package io.github.yeahfo.cor.customer.domain.events;

import io.github.yeahfo.cor.common.domain.Money;

public record CustomerCreatedEvent( String name,
                                    Money creditLimit ) implements CustomerDomainEvent {
    @Override
    public Long orderId( ) {
        return null;
    }
}
