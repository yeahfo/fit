package io.github.yeahfo.fit.core.order.domain;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.github.yeahfo.fit.core.common.utils.SnowflakeIdGenerator;
import io.github.yeahfo.fit.core.order.domain.events.OrderApprovedEvent;
import io.github.yeahfo.fit.core.order.domain.events.OrderCreatedEvent;
import io.github.yeahfo.fit.core.order.domain.events.OrderDomainEvent;
import io.github.yeahfo.fit.core.order.domain.events.OrderRejectedEvent;
import lombok.Getter;

import static io.github.yeahfo.fit.core.order.domain.OrderState.*;

@Getter
public class Order {
    private Long id;
    private Long version;
    private OrderState state;
    private OrderDetails details;

    public static ResultWithDomainEvents< Order, OrderDomainEvent > create( OrderDetails details ) {
        Order order = new Order( );
        order.id = SnowflakeIdGenerator.newSnowflakeId( );
        order.state = PENDING;
        order.details = details;
        return new ResultWithDomainEvents<>( order, new OrderCreatedEvent( details ) );
    }

    public ResultWithDomainEvents< Order, OrderDomainEvent > noteCreditReserved( ) {
        this.state = APPROVED;
        return new ResultWithDomainEvents<>( this, new OrderApprovedEvent( ) );
    }

    public ResultWithDomainEvents< Order, OrderDomainEvent > noteCreditReservationFailed( ) {
        this.state = REJECTED;
        return new ResultWithDomainEvents<>( this, new OrderRejectedEvent( ) );
    }
}
