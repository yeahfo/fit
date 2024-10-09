package io.github.yeahfo.fit.core.tenant.domain.events;

import io.github.yeahfo.fit.core.common.domain.user.User;

public record TenantInvoiceTitleUpdatedEvent( User releaser ) implements TenantUpdatedEvent {
}
