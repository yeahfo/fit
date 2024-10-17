package io.github.yeahfo.cor.order.domain;

import io.github.yeahfo.cor.common.domain.Money;

public record OrderDetails( Long customerId,
                            Money orderTotal ) {
}
