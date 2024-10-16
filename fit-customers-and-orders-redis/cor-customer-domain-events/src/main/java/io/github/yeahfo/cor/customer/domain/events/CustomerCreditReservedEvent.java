package io.github.yeahfo.cor.customer.domain.events;

public record CustomerCreditReservedEvent( Long orderId ) implements CustomerDomainEvent {
}
