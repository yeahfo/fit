package io.github.yeahfo.cor.order.domain;

import java.util.Optional;

public interface OrderRepository {
    Order save( Order order );

    Optional< Order > findById( Long id );

    default Order findBy( Long id ) {
        return findById( id ).orElseThrow( ( ) -> new RuntimeException( String.format( "Order %d not found.", id ) ) );
    }
}
