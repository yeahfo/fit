package io.github.yeahfo.cor.order.application.consumers;

import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import io.github.yeahfo.cor.customer.domain.events.CustomerCreditReservationFailedEvent;
import io.github.yeahfo.cor.customer.domain.events.CustomerCreditReservedEvent;
import io.github.yeahfo.cor.customer.domain.events.CustomerDomainEvent;
import io.github.yeahfo.cor.customer.domain.events.CustomerValidationFailedEvent;
import io.github.yeahfo.cor.order.application.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderApplicationEventSubscriber {
    private final OrderService orderService;

    public DomainEventHandlers domainEventHandlers( ) {
        return DomainEventHandlersBuilder
                .forAggregateType( CustomerDomainEvent.aggregateType )
                .onEvent( CustomerCreditReservedEvent.class, this::handleCustomerCreditReservedEvent )
                .onEvent( CustomerCreditReservationFailedEvent.class, this::handleCustomerCreditReservationFailedEvent )
                .onEvent( CustomerValidationFailedEvent.class, this::handleCustomerValidationFailedEvent )
                .build( );
    }

    private void handleCustomerValidationFailedEvent( DomainEventEnvelope< CustomerValidationFailedEvent > envelope ) {
        this.orderService.rejectOrder( envelope.getEvent( ).orderId( ) );
    }

    private void handleCustomerCreditReservationFailedEvent( DomainEventEnvelope< CustomerCreditReservationFailedEvent > envelope ) {
        this.orderService.rejectOrder( envelope.getEvent( ).orderId( ) );
    }

    private void handleCustomerCreditReservedEvent( DomainEventEnvelope< CustomerCreditReservedEvent > envelope ) {
        this.orderService.approveOrder( envelope.getEvent( ).orderId( ) );
    }
}
