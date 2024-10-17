package io.github.yeahfo.cor.order.domain.events;

import io.github.yeahfo.cor.order.domain.OrderDetails;

public record OrderRejectedEvent( OrderDetails orderDetails ) implements OrderDomainEvent {
}
