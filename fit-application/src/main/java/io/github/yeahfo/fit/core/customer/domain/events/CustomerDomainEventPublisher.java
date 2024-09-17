package io.github.yeahfo.fit.core.customer.domain.events;

import io.eventuate.tram.events.aggregates.AbstractAggregateDomainEventPublisher;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.github.yeahfo.fit.core.customer.domain.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerDomainEventPublisher extends AbstractAggregateDomainEventPublisher< Customer, CustomerDomainEvent > {
    protected CustomerDomainEventPublisher( DomainEventPublisher eventPublisher ) {
        super( eventPublisher, Customer.class, Customer::getId );
    }
}
