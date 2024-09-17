package io.github.yeahfo.fit.core.order.domain;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderDetails( Long customer,
                            BigDecimal total ) {
}
