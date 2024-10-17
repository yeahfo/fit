package io.github.yeahfo.cor.order.domain.events;

import io.github.yeahfo.cor.order.domain.OrderDetails;
import io.github.yeahfo.cor.order.domain.OrderState;

public record OrderCreatedEvent( OrderDetails orderDetails, OrderState state ) implements OrderDomainEvent {
}
