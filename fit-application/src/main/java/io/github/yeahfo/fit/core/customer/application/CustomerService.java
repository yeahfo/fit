package io.github.yeahfo.fit.core.customer.application;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.github.yeahfo.fit.core.customer.domain.Customer;
import io.github.yeahfo.fit.core.customer.domain.CustomerDomainService;
import io.github.yeahfo.fit.core.customer.domain.CustomerRepository;
import io.github.yeahfo.fit.core.customer.domain.events.CustomerDomainEvent;
import io.github.yeahfo.fit.core.customer.domain.events.CustomerDomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerDomainService customerDomain;
    private final CustomerRepository customerRepository;
    private final CustomerDomainEventPublisher domainEventPublisher;

    @Transactional
    public Long create( CreateCustomerCommand command ) {
        ResultWithDomainEvents< Customer, CustomerDomainEvent > resultWithDomainEvents = Customer.create( command.name( ), command.creditLimit( ) );
        Customer customer = customerRepository.save( resultWithDomainEvents.result );
        domainEventPublisher.publish( customer, resultWithDomainEvents.events );
        return customer.getId( );
    }

    @Transactional
    public void reserveCredit( Long id, long orderId, BigDecimal orderTotal ) {
        customerDomain.reserveCredit( id, orderId, orderTotal );
    }
}
