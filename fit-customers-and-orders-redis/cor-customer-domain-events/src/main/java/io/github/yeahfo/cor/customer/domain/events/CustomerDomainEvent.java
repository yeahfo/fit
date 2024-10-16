package io.github.yeahfo.cor.customer.domain.events;

import io.eventuate.tram.events.common.DomainEvent;

public interface CustomerDomainEvent extends DomainEvent {
    Long orderId( );

    String aggregateType = "io.github.yeahfo.cor.customer.domain.Customer";
}
