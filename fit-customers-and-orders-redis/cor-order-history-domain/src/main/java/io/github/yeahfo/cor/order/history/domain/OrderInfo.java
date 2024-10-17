package io.github.yeahfo.cor.order.history.domain;

import io.github.yeahfo.cor.common.domain.Money;
import io.github.yeahfo.cor.order.domain.OrderState;
import lombok.Builder;

@Builder
public record OrderInfo( Long id,
                         OrderState state,
                         Money orderTotal ) {
    public OrderInfo approve( ) {
        return OrderInfo.builder( )
                .id( id )
                .state( OrderState.APPROVED )
                .orderTotal( orderTotal )
                .build( );
    }

    public OrderInfo reject( ) {
        return OrderInfo.builder( )
                .id( id )
                .state( OrderState.REJECTED )
                .orderTotal( orderTotal )
                .build( );
    }
}
