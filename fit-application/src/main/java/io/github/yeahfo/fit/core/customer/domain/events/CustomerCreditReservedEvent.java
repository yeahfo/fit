package io.github.yeahfo.fit.core.customer.domain.events;

public record CustomerCreditReservedEvent( long orderId ) implements CustomerDomainEvent {
}
