package io.github.yeahfo.fit.core.order.application;

import io.github.yeahfo.fit.core.order.domain.OrderDetails;

public record CreateOrderCommand( OrderDetails details )  {
}
