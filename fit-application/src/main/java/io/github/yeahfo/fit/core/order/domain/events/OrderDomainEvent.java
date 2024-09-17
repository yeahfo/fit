package io.github.yeahfo.fit.core.order.domain.events;

import io.eventuate.tram.events.common.DomainEvent;

public interface OrderDomainEvent extends DomainEvent {
    String aggregateType = "io.github.yeahfo.fit.core.order.domain.Order";
}
