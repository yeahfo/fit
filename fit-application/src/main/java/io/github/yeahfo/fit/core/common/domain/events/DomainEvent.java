package io.github.yeahfo.fit.core.common.domain.events;

import io.github.yeahfo.fit.core.common.domain.user.User;

public interface DomainEvent extends io.eventuate.tram.events.common.DomainEvent {
    User releaser( );
}
