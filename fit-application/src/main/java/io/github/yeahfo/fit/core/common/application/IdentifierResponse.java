package io.github.yeahfo.fit.core.common.application;

import lombok.Builder;

@Builder
public record IdentifierResponse( String id ) {

    public static IdentifierResponse identifier( String id ) {
        return IdentifierResponse.builder( ).id( id ).build( );
    }

    @Override
    public String toString( ) {
        return id;
    }
}
