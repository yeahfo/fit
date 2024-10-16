package io.github.yeahfo.fit.common.utils;

import java.util.Collection;

import static com.google.common.collect.Iterables.isEmpty;

public interface Identified< Identifier > {
    Identifier identifier( );

    static boolean isDuplicated( Collection< ? extends Identified< ? > > collection ) {
        if ( isEmpty( collection ) ) {
            return false;
        }
        long count = collection.stream( ).map( Identified::identifier ).distinct( ).count( );
        return count != collection.size( );
    }
}
