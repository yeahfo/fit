package io.github.yeahfo.fit.core.tenant.application;

import io.github.yeahfo.fit.core.tenant.domain.invoice.InvoiceTitle;
import lombok.Builder;

@Builder
public record TenantInvoiceTitle( InvoiceTitle title ) {
}
