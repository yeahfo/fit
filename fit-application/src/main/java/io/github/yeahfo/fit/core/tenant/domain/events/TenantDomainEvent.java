package io.github.yeahfo.fit.core.tenant.domain.events;

import io.github.yeahfo.fit.core.common.domain.events.DomainEvent;

public interface TenantDomainEvent extends DomainEvent {
    String aggregateType
            = "io.github.yeahfo.fit.core.tenant.domain.Tenant";
}
