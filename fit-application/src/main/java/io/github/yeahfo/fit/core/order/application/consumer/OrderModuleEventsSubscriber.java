package io.github.yeahfo.fit.core.order.application.consumer;

import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import io.github.yeahfo.fit.core.customer.domain.events.*;
import io.github.yeahfo.fit.core.order.application.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderModuleEventsSubscriber {
    private final OrderService orderService;

    public DomainEventHandlers domainEventHandlers( ) {
        return DomainEventHandlersBuilder
                .forAggregateType( CustomerDomainEvent.aggregateType )
                .onEvent( CustomerCreatedEvent.class, this::handleCustomerCreatedEvent )
                .onEvent( CustomerCreditReservedEvent.class, this::handleCustomerCreditReservedEvent )
                .onEvent( CustomerValidationFailedEvent.class, this::handleCustomerValidationFailedEvent )
                .onEvent( CustomerCreditReservationFailedEvent.class, this::handleCustomerCreditReservationFailedEvent )
                .build( );
    }

    private void handleCustomerCreditReservationFailedEvent( DomainEventEnvelope< CustomerCreditReservationFailedEvent > envelope ) {
        orderService.rejectOrder( envelope.getEvent( ).orderId( ) );
    }

    private void handleCustomerValidationFailedEvent( DomainEventEnvelope< CustomerValidationFailedEvent > envelope ) {
        orderService.rejectOrder( envelope.getEvent( ).orderId( ) );
    }

    private void handleCustomerCreditReservedEvent( DomainEventEnvelope< CustomerCreditReservedEvent > envelope ) {
        orderService.approveOrder( envelope.getEvent( ).orderId( ) );
    }

    private void handleCustomerCreatedEvent( DomainEventEnvelope< CustomerCreatedEvent > envelope ) {
        log.info( "Noop handle CustomerCreatedEvent logging the envelope: \t\n{}", envelope );
    }
}
