package io.github.yeahfo.cor.customer.application.consumer;

import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import io.github.yeahfo.cor.customer.application.CustomerService;
import io.github.yeahfo.cor.order.domain.OrderDetails;
import io.github.yeahfo.cor.order.domain.events.OrderCreatedEvent;
import io.github.yeahfo.cor.order.domain.events.OrderDomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerApplicationEventSubscriber {
    private final CustomerService customerService;

    public DomainEventHandlers domainEventHandlers( ) {
        return DomainEventHandlersBuilder
                .forAggregateType( OrderDomainEvent.aggregateType )
                .onEvent( OrderCreatedEvent.class, this::handleOrderCreatedEvent )
                .build( );
    }

    private void handleOrderCreatedEvent( DomainEventEnvelope< OrderCreatedEvent > envelope ) {
        long orderId = Long.parseLong( envelope.getAggregateId( ) );
        OrderDetails orderDetails = envelope.getEvent( ).orderDetails( );
        customerService.reserveCustomerCredit( orderDetails.customerId( ), orderId, orderDetails.orderTotal( ) );
    }
}
