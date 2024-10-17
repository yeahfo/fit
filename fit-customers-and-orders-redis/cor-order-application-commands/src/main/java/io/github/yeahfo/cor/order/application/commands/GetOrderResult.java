package io.github.yeahfo.cor.order.application.commands;

import io.github.yeahfo.cor.order.domain.OrderDetails;
import io.github.yeahfo.cor.order.domain.OrderState;
import lombok.Builder;

@Builder
public record GetOrderResult( Long orderId,
                              OrderState orderState,
                              OrderDetails orderDetails ) {
}
