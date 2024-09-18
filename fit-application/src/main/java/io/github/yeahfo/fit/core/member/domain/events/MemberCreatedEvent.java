package io.github.yeahfo.fit.core.member.domain.events;

import io.github.yeahfo.fit.core.common.domain.user.User;

public record MemberCreatedEvent( User releaser ) implements MemberDomainEvent {
}
