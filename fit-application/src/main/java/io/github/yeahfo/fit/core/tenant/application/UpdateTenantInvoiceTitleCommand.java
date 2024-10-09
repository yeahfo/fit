package io.github.yeahfo.fit.core.tenant.application;

import io.github.yeahfo.fit.core.common.application.Command;
import io.github.yeahfo.fit.core.tenant.domain.invoice.InvoiceTitle;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateTenantInvoiceTitleCommand( @Valid
                                               @NotNull
                                               InvoiceTitle title ) implements Command {
    @Override
    public void correctAndValidate( ) {

    }
}
