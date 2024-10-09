package io.github.yeahfo.fit.core.tenant.application;

import io.github.yeahfo.fit.core.common.application.Command;
import io.github.yeahfo.fit.core.order.domain.delivery.Consignee;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateConsigneeCommand( @Valid
                                      @NotNull
                                      Consignee consignee ) implements Command {
    @Override
    public void correctAndValidate( ) {

    }
}
