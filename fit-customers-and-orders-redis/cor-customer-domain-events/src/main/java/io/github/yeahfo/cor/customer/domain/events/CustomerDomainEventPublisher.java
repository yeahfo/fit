package io.github.yeahfo.cor.customer.domain.events;

import io.eventuate.tram.events.aggregates.AbstractAggregateDomainEventPublisher;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.github.yeahfo.cor.common.domain.Money;
import io.github.yeahfo.cor.customer.domain.Customer;

import java.util.Arrays;
import java.util.List;

public class CustomerDomainEventPublisher extends AbstractAggregateDomainEventPublisher< Customer, CustomerDomainEvent > {
    private final DomainEventPublisher domainEventPublisher;

    public CustomerDomainEventPublisher( DomainEventPublisher eventPublisher ) {
        super( eventPublisher, Customer.class, Customer::identifier );
        domainEventPublisher = eventPublisher;
    }

    public void publishCustomerCreatedEvent( Customer customer, String name, Money money ) {
        this.publish( customer, events( new CustomerCreatedEvent( name, money ) ) );
    }

    public void publishCustomerCreditReservedEvent( Customer customer, long orderId, Money subtracted ) {
        this.publish( customer, events( new CustomerCreditReservedEvent( orderId, subtracted ) ) );
    }

    public void publishCustomerCreditReservationFailedEvent( Customer customer, long orderId ) {
        this.publish( customer, events( new CustomerCreditReservationFailedEvent( orderId ) ) );
    }

    public void publishCustomerValidationFailedEvent( long customerId, long orderId ) {
        this.domainEventPublisher.publish( Customer.class, customerId, List.of( new CustomerValidationFailedEvent( orderId ) ) );
    }

    private static List< CustomerDomainEvent > events( CustomerDomainEvent... events ) {
        return Arrays.stream( events ).toList( );
    }
}
