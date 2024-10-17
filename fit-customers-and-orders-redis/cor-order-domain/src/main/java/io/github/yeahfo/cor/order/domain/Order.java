package io.github.yeahfo.cor.order.domain;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.github.yeahfo.cor.common.domain.AggregateRoot;
import io.github.yeahfo.cor.order.domain.events.OrderApprovedEvent;
import io.github.yeahfo.cor.order.domain.events.OrderCreatedEvent;
import io.github.yeahfo.cor.order.domain.events.OrderDomainEvent;
import io.github.yeahfo.cor.order.domain.events.OrderRejectedEvent;
import io.github.yeahfo.fit.common.utils.SnowflakeIdGenerator;

import java.util.function.Supplier;

public class Order extends AggregateRoot< Long > {
    private OrderState state;
    private OrderDetails orderDetails;

    public static ResultWithDomainEvents< Order, OrderDomainEvent > create( OrderDetails orderDetails ) {
        Order order = new Order( );
        order.state = OrderState.PENDING;
        order.orderDetails = orderDetails;
        order.initialize( orderDetails.customerId( ), null );
        return new ResultWithDomainEvents<>( order, new OrderCreatedEvent( orderDetails, order.state ) );
    }

    public ResultWithDomainEvents< Order, OrderDomainEvent > approve( ) {
        this.state = OrderState.APPROVED;
        return new ResultWithDomainEvents<>( this, new OrderApprovedEvent( orderDetails ) );
    }

    public ResultWithDomainEvents< Order, OrderDomainEvent > reject( ) {
        this.state = OrderState.REJECTED;
        return new ResultWithDomainEvents<>( this, new OrderRejectedEvent( orderDetails ) );
    }

    @Override
    protected Supplier< Long > idGenerator( ) {
        return SnowflakeIdGenerator::newSnowflakeId;
    }

    public OrderState state( ) {
        return state;
    }

    public OrderDetails orderDetails( ) {
        return orderDetails;
    }
}
