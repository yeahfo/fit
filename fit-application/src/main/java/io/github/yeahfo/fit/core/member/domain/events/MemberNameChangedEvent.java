package io.github.yeahfo.fit.core.member.domain.events;

import io.github.yeahfo.fit.core.common.domain.user.User;

public record MemberNameChangedEvent( String newName, User releaser ) implements MemberDomainEvent {
}
