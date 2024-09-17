package io.github.yeahfo.fit.core.customer.domain;

import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.github.yeahfo.fit.core.customer.domain.events.CustomerCreditReservationFailedEvent;
import io.github.yeahfo.fit.core.customer.domain.events.CustomerCreditReservedEvent;
import io.github.yeahfo.fit.core.customer.domain.events.CustomerValidationFailedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerDomainService {
    private final CustomerRepository customerRepository;
    private final DomainEventPublisher domainEventPublisher;

    public void reserveCredit( Long id,
                               long orderId,
                               BigDecimal orderTotal ) {

        Optional< Customer > possibleCustomer = customerRepository.findById( id );

        if ( possibleCustomer.isEmpty( ) ) {
            log.error( "Non-existent customer: {}", id );
            domainEventPublisher.publish( Customer.class, id,
                    Collections.singletonList( new CustomerValidationFailedEvent( orderId ) ) );
            return;
        }
        Customer customer = possibleCustomer.get( );
        try {
            customer.reserveCredit( orderId, orderTotal );
            customerRepository.save( customer );
            domainEventPublisher.publish( Customer.class, customer.getId( ),
                    Collections.singletonList( new CustomerCreditReservedEvent( orderId ) ) );
        } catch ( CustomerCreditLimitExceededException e ) {
            CustomerCreditReservationFailedEvent customerCreditReservationFailedEvent =
                    new CustomerCreditReservationFailedEvent( orderId );
            domainEventPublisher.publish( Customer.class, customer.getId( ),
                    Collections.singletonList( customerCreditReservationFailedEvent ) );
        }
    }
}
