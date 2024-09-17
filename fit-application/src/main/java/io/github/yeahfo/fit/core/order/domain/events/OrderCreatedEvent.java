package io.github.yeahfo.fit.core.order.domain.events;

import io.github.yeahfo.fit.core.order.domain.OrderDetails;

public record OrderCreatedEvent( OrderDetails details ) implements OrderDomainEvent {
}
