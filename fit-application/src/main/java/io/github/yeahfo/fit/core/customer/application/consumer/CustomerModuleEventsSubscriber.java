package io.github.yeahfo.fit.core.customer.application.consumer;

import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import io.github.yeahfo.fit.core.customer.application.CustomerService;
import io.github.yeahfo.fit.core.order.domain.events.OrderCreatedEvent;
import io.github.yeahfo.fit.core.order.domain.events.OrderDomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerModuleEventsSubscriber {
    private final CustomerService customerService;

    public DomainEventHandlers domainEventHandlers( ) {
        return DomainEventHandlersBuilder
                .forAggregateType( OrderDomainEvent.aggregateType )
                .onEvent( OrderCreatedEvent.class, this::orderCreatedHandler )
                .build( );
    }

    private void orderCreatedHandler( DomainEventEnvelope< OrderCreatedEvent > envelope ) {
        long orderId = Long.parseLong( envelope.getAggregateId( ) );
        OrderCreatedEvent event = envelope.getEvent( );
        customerService.reserveCredit(event.details().customer(), orderId, event.details().total() );
    }
}
