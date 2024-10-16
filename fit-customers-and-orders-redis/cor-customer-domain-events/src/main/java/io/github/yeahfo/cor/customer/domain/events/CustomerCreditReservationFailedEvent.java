package io.github.yeahfo.cor.customer.domain.events;

public record CustomerCreditReservationFailedEvent( Long orderId ) implements CustomerDomainEvent {
}
