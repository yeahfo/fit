package io.github.yeahfo.fit.core.customer.domain.events;

import io.eventuate.tram.events.common.DomainEvent;

public interface CustomerDomainEvent extends DomainEvent {
    String aggregateType = "io.github.yeahfo.fit.core.customer.domain.Customer";
}
