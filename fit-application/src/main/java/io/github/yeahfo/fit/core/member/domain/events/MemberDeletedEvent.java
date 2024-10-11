package io.github.yeahfo.fit.core.member.domain.events;

import io.github.yeahfo.fit.core.common.domain.user.User;

public record MemberDeletedEvent( User releaser ) implements MemberDomainEvent {
}
