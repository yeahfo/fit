package io.github.yeahfo.fit.core.customer.domain.events;

public record CustomerCreditReservationFailedEvent( Long orderId ) implements CustomerDomainEvent {
}
