package io.github.yeahfo.cor.order.domain.events;

import io.eventuate.tram.events.aggregates.AbstractAggregateDomainEventPublisher;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.github.yeahfo.cor.order.domain.Order;


public class OrderDomainEventPublisher extends AbstractAggregateDomainEventPublisher< Order, OrderDomainEvent > {
    public OrderDomainEventPublisher( DomainEventPublisher eventPublisher ) {
        super( eventPublisher, Order.class, Order::identifier );
    }
}
