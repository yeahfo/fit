package io.github.yeahfo.cor.customer.application;

import io.github.yeahfo.cor.common.domain.Money;
import io.github.yeahfo.cor.customer.application.commands.CreateCustomerCommand;
import io.github.yeahfo.cor.customer.application.commands.CreateCustomerResult;
import io.github.yeahfo.cor.customer.domain.Customer;
import io.github.yeahfo.cor.customer.domain.CustomerCreditLimitExceededException;
import io.github.yeahfo.cor.customer.domain.events.CustomerDomainEventPublisher;
import io.github.yeahfo.cor.customer.domain.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerDomainEventPublisher domainEventPublisher;

    @Transactional
    public CreateCustomerResult createCustomer( CreateCustomerCommand command ) {
        Customer customer = customerRepository.save( Customer.create( command.name( ), command.creditLimit( ) ) );
        domainEventPublisher.publishCustomerCreatedEvent( customer, command.name( ), command.creditLimit( ) );
        return new CreateCustomerResult( customer.identifier( ) );
    }

    @Transactional
    public void reserveCustomerCredit( Long customerId, long orderId, Money orderTotal ) {
        Optional< Customer > finder = this.customerRepository.findById( customerId );
        if ( finder.isPresent( ) ) {
            Customer customer = finder.get( );
            try {
                customer.reserveCredit( orderId, orderTotal );
                this.customerRepository.save( customer );
                domainEventPublisher.publishCustomerCreditReservedEvent( customer, orderId, orderTotal );
            } catch ( CustomerCreditLimitExceededException e ) {
                domainEventPublisher.publishCustomerCreditReservationFailedEvent( customer, orderId );
            }
            return;
        }

        domainEventPublisher.publishCustomerValidationFailedEvent( customerId, orderId );
    }
}
