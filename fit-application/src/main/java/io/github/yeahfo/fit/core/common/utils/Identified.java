package io.github.yeahfo.fit.core.common.utils;

import java.util.Collection;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;

public interface Identified< Identifier > {
    static boolean isDuplicated( Collection< ? extends Identified< ? > > collection ) {
        if ( isEmpty( collection ) ) {
            return false;
        }
        long count = collection.stream( ).map( Identified::identifier ).distinct( ).count( );
        return count != collection.size( );
    }

    Identifier identifier( );
}
