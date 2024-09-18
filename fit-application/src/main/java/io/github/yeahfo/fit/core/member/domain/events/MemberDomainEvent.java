package io.github.yeahfo.fit.core.member.domain.events;

import io.github.yeahfo.fit.core.common.domain.events.DomainEvent;

public interface MemberDomainEvent extends DomainEvent {
    String aggregateType
            = "io.github.yeahfo.fit.core.member.domain.Member";
}
