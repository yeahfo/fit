package io.github.yeahfo.cor.order.domain.events;

import io.eventuate.tram.events.common.DomainEvent;
import io.github.yeahfo.cor.order.domain.Order;
import io.github.yeahfo.cor.order.domain.OrderDetails;

public interface OrderDomainEvent extends DomainEvent {
    String aggregateType = Order.class.getName();

    OrderDetails orderDetails( );
}
