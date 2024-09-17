package io.github.yeahfo.fit.common.mongo.messaging;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings( { "UnusedReturnValue", "unused" } )
public interface DataProducer {
    CompletableFuture< ? > send( String topic, String key, String body );

    void close( );
}