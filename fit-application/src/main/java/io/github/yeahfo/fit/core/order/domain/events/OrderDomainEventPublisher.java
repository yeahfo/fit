package io.github.yeahfo.fit.core.order.domain.events;

import io.eventuate.tram.events.aggregates.AbstractAggregateDomainEventPublisher;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.github.yeahfo.fit.core.order.domain.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderDomainEventPublisher extends AbstractAggregateDomainEventPublisher< Order, OrderDomainEvent > {
    protected OrderDomainEventPublisher( DomainEventPublisher eventPublisher ) {
        super( eventPublisher, Order.class, Order::getId );
    }
}
