package io.github.yeahfo.fit.common.mongo.messaging;

import io.eventuate.common.id.ApplicationIdGenerator;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.messaging.producer.common.MessageProducerImplementation;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.Instant;
import java.util.Map;

import static io.eventuate.tram.messaging.common.Message.DESTINATION;
import static io.eventuate.tram.messaging.common.Message.ID;


/**
 * -- message table
 * CREATE SEQUENCE IF NOT EXISTS eventuate.message_table_id_sequence START 1;
 * SELECT setval('eventuate.message_table_id_sequence', (ROUND(EXTRACT(EPOCH FROM CURRENT_TIMESTAMP) * 1000)) :: BIGINT);
 * CREATE TABLE IF NOT EXISTS eventuate.message
 * (
 * dbid              BIGINT NOT NULL DEFAULT nextval('eventuate.message_table_id_sequence') PRIMARY KEY,
 * id                VARCHAR,
 * destination       TEXT   NOT NULL,
 * headers           TEXT   NOT NULL,
 * payload           TEXT   NOT NULL,
 * published         SMALLINT        DEFAULT 0,
 * message_partition SMALLINT,
 * creation_time     BIGINT
 * );
 * ALTER SEQUENCE eventuate.message_table_id_sequence OWNED BY eventuate.message.dbid;
 */
@RequiredArgsConstructor
public class MessageProducerMongoImpl implements MessageProducerImplementation {
    public static String MESSAGE_COLLECTION_NAME = "_message";

    @Builder
    public record MongoMessage( String id,
                                String destination,
                                Map< String, String > headers,
                                String payload,
                                Integer published,
                                Integer message_partition,
                                Long creation_time ) {

    }

    public static ApplicationIdGenerator messageIdGenerator = new ApplicationIdGenerator( );
    private final MongoTemplate mongoTemplate;

    @Override
    public void send( Message message ) {
        String messageId = messageIdGenerator.genId( ).asString( );
        message.setHeader( ID, messageId );
        MongoMessage mongoMessage = MongoMessage.builder( )
                .id( messageId )
                .destination( message.getRequiredHeader( DESTINATION ) )
                .headers( message.getHeaders( ) )
                .payload( message.getPayload( ) )
                .published( 0 )
                .creation_time( Instant.now( ).toEpochMilli( ) )
                .build( );
        mongoTemplate.save( mongoMessage, MESSAGE_COLLECTION_NAME );
    }
}
