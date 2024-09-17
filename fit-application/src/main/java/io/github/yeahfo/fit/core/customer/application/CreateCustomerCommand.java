package io.github.yeahfo.fit.core.customer.application;

import java.math.BigDecimal;

public record CreateCustomerCommand( String name, BigDecimal creditLimit ) {
}
