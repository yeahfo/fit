package io.github.yeahfo.cor.order.history.domain;

import io.github.yeahfo.cor.common.domain.Money;
import io.github.yeahfo.cor.order.domain.OrderState;

public record OrderInfo( Long id,
                         OrderState state,
                         Money orderTotal ) {
}
