package io.github.yeahfo.fit.common.mongo.messaging;

import com.mongodb.client.model.changestream.ChangeStreamDocument;
import io.eventuate.messaging.redis.spring.producer.EventuateRedisProducer;
import io.eventuate.tram.messaging.producer.common.MessageProducerImplementation;
import io.eventuate.tram.spring.messaging.producer.common.TramMessagingCommonProducerConfiguration;
import io.github.yeahfo.fit.common.jackson.Jackson;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.messaging.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.NonNull;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

import static io.eventuate.tram.messaging.common.Message.PARTITION_ID;
import static io.github.yeahfo.fit.common.mongo.messaging.MessageProducerMongoImpl.MongoMessage;
import static io.github.yeahfo.fit.common.mongo.messaging.MessageProducerMongoImpl.MESSAGE_COLLECTION_NAME;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Configuration
@Import( {
        TramMessagingCommonProducerConfiguration.class,
} )
public class TramMessageProducerMongoConfiguration {

    @Bean
    @ConditionalOnMissingBean( MessageProducerImplementation.class )
    public MessageProducerImplementation messageProducerImplementation( MongoTemplate mongoTemplate ) {
        return new MessageProducerMongoImpl( mongoTemplate );
    }

    @Bean
    MessageListenerContainer messageListenerContainer( MongoTemplate template,
                                                       DataProducerFactory dataProducerFactory ) throws InterruptedException {
        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer( template ) {
            /* auto startup will be changed for M2, so this should no longer be required. */
            @Override
            public boolean isAutoStartup( ) {
                return true;
            }
        };
        MessageListener< ChangeStreamDocument< Document >, MongoMessage > messageListener = new MessageListener<>( ) {
            private final DataProducer dataProducer = dataProducerFactory.create( );

            @Override
            public void onMessage( @NonNull Message< ChangeStreamDocument< Document >, MongoMessage > message ) {
                Optional.ofNullable( message.getBody( ) ).ifPresent( then ->
                        dataProducer.send( then.destination( ), then.headers( ).get( PARTITION_ID ),
                        Jackson.writeValueAsString( Map.of( "payload", then.payload( ), "headers", then.headers( ) ) ) ) );
            }

        };
        ChangeStreamRequest< MongoMessage > changeStreamRequest = ChangeStreamRequest.builder( messageListener ) //
                .collection( MESSAGE_COLLECTION_NAME ) //
                .filter( newAggregation( match( where( "operationType" ).is( "insert" ) ) ) ) // we are only interested in inserts
                .build( );
        Subscription subscription = container.register( changeStreamRequest, MongoMessage.class );
        subscription.await( Duration.ofMillis( 200 ) ); // wait till the subscription becomes active
        return container;
    }

    @Bean
    public DataProducerFactory redisDataProducerFactory( StringRedisTemplate redisTemplate,
                                                         @Value( "${eventuate.redis.partitions:#{1}}" ) int partitions ) {
        return ( ) -> new EventuateRedisDataProducerWrapper( new EventuateRedisProducer( redisTemplate, partitions ) );
    }
}
