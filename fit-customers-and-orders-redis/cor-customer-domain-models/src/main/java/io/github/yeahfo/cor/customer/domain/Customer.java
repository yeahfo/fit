package io.github.yeahfo.cor.customer.domain;

import io.github.yeahfo.cor.common.domain.AggregateRoot;
import io.github.yeahfo.cor.common.domain.Money;
import io.github.yeahfo.fit.common.utils.SnowflakeIdGenerator;

import java.util.Map;
import java.util.function.Supplier;

import static java.util.Collections.emptyMap;

public class Customer extends AggregateRoot< Long > {
    protected String name;
    protected Money creditLimit;
    protected Map< Long, Money > creditReservations;

    @Override
    protected Supplier< Long > idGenerator( ) {
        return SnowflakeIdGenerator::newSnowflakeId;
    }

    Money availableCredit( ) {
        return creditLimit.subtract( creditReservations.values( ).stream( ).reduce( Money.ZERO, Money::add ) );
    }

    public static Customer create( String name, Money creditLimit ) {
        Customer customer = new Customer( );
        customer.name = name;
        customer.creditLimit = creditLimit;
        customer.creditReservations = emptyMap( );
        customer.initialize( customer.identifier( ), name );
        return customer;
    }

    public void reserveCredit( long orderId, Money orderTotal ) throws CustomerCreditLimitExceededException {
        if ( !availableCredit( ).isGreaterThanOrEqual( orderTotal ) ) {
            throw new CustomerCreditLimitExceededException( );
        }
        creditReservations.put( orderId, orderTotal );
    }
}
