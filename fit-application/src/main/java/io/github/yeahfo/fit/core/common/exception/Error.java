package io.github.yeahfo.fit.core.common.exception;

import java.time.Instant;
import java.util.Map;

import static java.time.Instant.now;

public record Error( ErrorCode code,
                     String message,
                     String userMessage,
                     int status,
                     String path,
                     Instant timestamp,
                     String traceId,
                     Map< String, Object > data ) {

    public Error( ErrorCode code, int status, String message, String path, String traceId, Map< String, Object > data ) {
        this( code, message, message, status, path, now( ), traceId, data );
    }

    public ErrorResponse toErrorResponse( ) {
        return ErrorResponse.builder( ).error( this ).build( );
    }
}
