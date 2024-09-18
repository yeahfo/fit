package io.github.yeahfo.fit.core.order.domain.delivery;

import io.github.yeahfo.fit.core.common.domain.Address;
import io.github.yeahfo.fit.core.common.utils.Identified;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static io.github.yeahfo.fit.core.common.utils.FitConstants.MAX_GENERIC_NAME_LENGTH;

public record Consignee( @NotBlank String id,
                         @NotBlank
                         @Size( max = MAX_GENERIC_NAME_LENGTH )
                         String name,
                         @NotBlank
                         String mobile,
                         @Valid
                         @NotNull
                         Address address ) implements Identified< String > {
    @Override
    public String identifier( ) {
        return id;
    }
}
