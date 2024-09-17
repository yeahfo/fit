package io.github.yeahfo.fit.core.customer.domain;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.github.yeahfo.fit.core.common.utils.SnowflakeIdGenerator;
import io.github.yeahfo.fit.core.customer.domain.events.CustomerCreatedEvent;
import io.github.yeahfo.fit.core.customer.domain.events.CustomerDomainEvent;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

@Getter
public class Customer {
    private Long id;
    private Long version;
    private String name;
    private BigDecimal creditLimit;
    private Map< Long, BigDecimal > creditReservations;

    BigDecimal availableCredit( ) {
        return creditLimit.subtract( creditReservations.values( ).stream( ).reduce( BigDecimal.ZERO, BigDecimal::add ) );
    }

    public static ResultWithDomainEvents< Customer, CustomerDomainEvent > create( String name, BigDecimal creditLimit ) {
        Customer customer = new Customer( );
        customer.id = SnowflakeIdGenerator.newSnowflakeId( );
        customer.name = name;
        customer.creditLimit = creditLimit;
        customer.creditReservations = Collections.emptyMap( );
        return new ResultWithDomainEvents<>( customer, new CustomerCreatedEvent( name, creditLimit, customer.creditReservations ) );
    }

    public void reserveCredit( long orderId, BigDecimal orderTotal ) {
        if ( !availableCreditIsGreaterThanOrEqual( orderTotal ) ) {
            throw new CustomerCreditLimitExceededException( );
        }
        creditReservations.put( orderId, orderTotal );
    }

    private boolean availableCreditIsGreaterThanOrEqual( BigDecimal orderTotal ) {
        return availableCredit( ).compareTo( orderTotal ) >= 0;
    }
}
