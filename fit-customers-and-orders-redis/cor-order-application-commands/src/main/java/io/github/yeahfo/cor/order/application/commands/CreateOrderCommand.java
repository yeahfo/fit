package io.github.yeahfo.cor.order.application.commands;

import io.github.yeahfo.cor.common.domain.Money;
import lombok.Builder;

@Builder
public record CreateOrderCommand( Long customerId,
                                  Money orderTotal ) {
}
