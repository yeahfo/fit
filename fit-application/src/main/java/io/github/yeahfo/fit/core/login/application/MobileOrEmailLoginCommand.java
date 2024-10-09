package io.github.yeahfo.fit.core.login.application;

import io.github.yeahfo.fit.core.common.application.Command;
import io.github.yeahfo.fit.core.common.validation.mobileoremail.MobileOrEmail;
import io.github.yeahfo.fit.core.common.validation.password.Password;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import static io.github.yeahfo.fit.management.FitManageTenant.ADMIN_INIT_MOBILE;
import static io.github.yeahfo.fit.management.FitManageTenant.ADMIN_INIT_PASSWORD;

@Builder
public record MobileOrEmailLoginCommand( @NotBlank
                                         @MobileOrEmail
                                         @Schema( defaultValue = ADMIN_INIT_MOBILE )
                                         String mobileOrEmail,
                                         @NotBlank
                                         @Password
                                         @Schema( defaultValue = ADMIN_INIT_PASSWORD )
                                         String password ) implements Command {
    @Override
    public void correctAndValidate( ) {

    }
}
