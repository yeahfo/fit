package io.github.yeahfo.cor.order.history.application.consumers;

import io.eventuate.tram.events.common.DomainEvent;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import io.github.yeahfo.cor.customer.domain.events.CustomerCreatedEvent;
import io.github.yeahfo.cor.customer.domain.events.CustomerDomainEvent;
import io.github.yeahfo.cor.order.history.application.OrderHistoryViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderHistoryApplicationEventSubscriber {
    private final OrderHistoryViewService orderHistoryViewService;

    public DomainEventHandlers domainEventHandlers( ) {
        return DomainEventHandlersBuilder
                .forAggregateType( CustomerDomainEvent.aggregateType )
                .onEvent( CustomerCreatedEvent.class, this::handleCustomerCreatedEvent )
                .build( );
    }


    private void handleCustomerCreatedEvent( DomainEventEnvelope< CustomerCreatedEvent > envelope ) {
        CustomerCreatedEvent event = envelope.getEvent( );
        orderHistoryViewService.createCustomer( this.parseLongAggregateId( envelope ),
                event.name( ), event.creditLimit( ) );
    }

    private long parseLongAggregateId( DomainEventEnvelope< ? extends DomainEvent > envelope ) {
        return Long.parseLong( envelope.getAggregateId( ) );
    }
}
