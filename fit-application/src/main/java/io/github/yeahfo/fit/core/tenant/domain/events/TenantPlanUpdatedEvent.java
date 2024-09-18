package io.github.yeahfo.fit.core.tenant.domain.events;

import io.github.yeahfo.fit.core.common.domain.user.User;
import io.github.yeahfo.fit.core.plan.domain.PlanType;

public record TenantPlanUpdatedEvent( PlanType planType, User releaser ) implements TenantDomainEvent {
}
