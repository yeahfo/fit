package io.github.yeahfo.fit.core.order.domain;

import java.util.Optional;

public interface OrderRepository {
    Order save( Order order );

    Optional< Order > findById( Long id );

    default Order findOrder( long id ) {
        return findById( id ).orElseThrow( ( ) -> new IllegalArgumentException( String.format( "Non-existent order: %d", id ) ) );
    }
}
