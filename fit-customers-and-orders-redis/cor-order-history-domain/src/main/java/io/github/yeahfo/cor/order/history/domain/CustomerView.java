package io.github.yeahfo.cor.order.history.domain;

import io.github.yeahfo.cor.common.domain.Money;
import io.github.yeahfo.cor.order.domain.OrderState;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public record CustomerView( Long id,
                            String name,
                            Money creditLimit,
                            Map< Long, OrderInfo > orders ) {

    public CustomerView( Long id, String name, Money creditLimit ) {
        this( id, name, creditLimit, new HashMap<>( ) );
    }

    public CustomerView addOrder( Long orderId, Money orderTotal ) {
        this.orders.put( orderId,
                OrderInfo.builder( )
                        .id( orderId )
                        .orderTotal( orderTotal )
                        .state( OrderState.PENDING )
                        .build( ) );
        return this;
    }

    public void approveOrder( Long orderId ) {
        Optional.ofNullable( orders.get( orderId ) ).ifPresent( order -> {
            OrderInfo approved = order.approve( );
            orders.put( orderId, approved );
        } );
    }

    public void rejectOrder( Long orderId ) {
        Optional.ofNullable( orders.get( orderId ) ).ifPresent( order -> {
            OrderInfo approved = order.reject( );
            orders.put( orderId, approved );
        } );
    }
}
