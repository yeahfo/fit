package io.github.yeahfo.fit.core.tenant.application;

import io.github.yeahfo.fit.core.common.domain.user.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CreateTenantCommand( @NotBlank String name,
                                   User user ) {
}
