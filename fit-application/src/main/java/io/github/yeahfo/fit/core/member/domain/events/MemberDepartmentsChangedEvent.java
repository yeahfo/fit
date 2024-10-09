package io.github.yeahfo.fit.core.member.domain.events;

import io.github.yeahfo.fit.core.common.domain.user.User;

import java.util.Set;

public record MemberDepartmentsChangedEvent( Set< String > removedDepartmentIds,
                                             Set< String > addedDepartmentIds,
                                             User releaser ) implements MemberDomainEvent {
}
