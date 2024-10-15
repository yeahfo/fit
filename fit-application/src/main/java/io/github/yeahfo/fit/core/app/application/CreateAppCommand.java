package io.github.yeahfo.fit.core.app.application;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import static io.github.yeahfo.fit.core.common.utils.FitConstants.MAX_GENERIC_NAME_LENGTH;

@Builder
public record CreateAppCommand( @NotBlank
                                @Size( max = MAX_GENERIC_NAME_LENGTH )
                                String name ) {
}
