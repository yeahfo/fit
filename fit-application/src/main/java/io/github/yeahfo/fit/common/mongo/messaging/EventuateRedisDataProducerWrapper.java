package io.github.yeahfo.fit.common.mongo.messaging;

import io.eventuate.messaging.redis.spring.producer.EventuateRedisProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class EventuateRedisDataProducerWrapper implements DataProducer {
    private final Logger logger = LoggerFactory.getLogger( getClass( ) );

    private final EventuateRedisProducer eventuateRedisProducer;

    public EventuateRedisDataProducerWrapper( EventuateRedisProducer eventuateRedisProducer ) {
        this.eventuateRedisProducer = eventuateRedisProducer;
    }

    @Override
    public CompletableFuture< ? > send( String topic, String key, String body ) {
        return eventuateRedisProducer.send( topic, key, body );
    }

    @Override
    public void close( ) {
        logger.info( "closing EventuateRedisDataProducerWrapper" );
        eventuateRedisProducer.close( );
        logger.info( "closed EventuateRedisDataProducerWrapper" );
    }
}
