package io.github.yeahfo.fit.core.customer.domain.events;

public record CustomerValidationFailedEvent( long orderId ) implements CustomerDomainEvent {
}
