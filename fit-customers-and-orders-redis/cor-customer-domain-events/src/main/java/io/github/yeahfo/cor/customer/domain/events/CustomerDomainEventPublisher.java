package io.github.yeahfo.cor.customer.domain.events;

import io.eventuate.tram.events.aggregates.AbstractAggregateDomainEventPublisher;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.github.yeahfo.cor.common.domain.Money;
import io.github.yeahfo.cor.customer.domain.Customer;

import java.util.Arrays;
import java.util.List;

public class CustomerDomainEventPublisher extends AbstractAggregateDomainEventPublisher< Customer, CustomerDomainEvent > {
    public CustomerDomainEventPublisher( DomainEventPublisher eventPublisher ) {
        super( eventPublisher, Customer.class, Customer::identifier );
    }

    public void publishCustomerCreatedEvent( Customer customer, String name, Money money ) {
        this.publish( customer, events( new CustomerCreatedEvent( name, money ) ) );
    }

    private static List< CustomerDomainEvent > events( CustomerDomainEvent... events ) {
        return Arrays.stream( events ).toList( );
    }
}
