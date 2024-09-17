package io.github.yeahfo.fit.core.customer.domain.events;

import java.math.BigDecimal;
import java.util.Map;

public record CustomerCreatedEvent( String name,
                                    BigDecimal creditLimit,
                                    Map< Long, BigDecimal > creditReservations ) implements CustomerDomainEvent {
}
