package io.github.yeahfo.cor.order.domain.events;

import io.github.yeahfo.cor.order.domain.OrderDetails;

public record OrderApprovedEvent( OrderDetails orderDetails ) implements OrderDomainEvent {
}
