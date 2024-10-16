package io.github.yeahfo.cor.customer.application.commands;

import io.github.yeahfo.cor.common.domain.Money;

public record CreateCustomerCommand( String name,
                                     Money creditLimit ) {
}
