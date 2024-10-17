package io.github.yeahfo.cor.customer.domain.events;

import io.github.yeahfo.cor.common.domain.Money;

public record CustomerCreditReservedEvent( Long orderId, Money subtracted ) implements CustomerDomainEvent {
}
