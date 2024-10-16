package io.github.yeahfo.cor.customer.domain.events;

public record CustomerValidationFailedEvent( Long orderId ) implements CustomerDomainEvent {
}
