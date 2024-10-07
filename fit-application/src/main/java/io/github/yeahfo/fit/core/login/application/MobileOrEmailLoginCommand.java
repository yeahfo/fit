package io.github.yeahfo.fit.core.login.application;

import io.github.yeahfo.fit.core.common.application.Command;
import io.github.yeahfo.fit.core.common.validation.mobileoremail.MobileOrEmail;
import io.github.yeahfo.fit.core.common.validation.password.Password;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record MobileOrEmailLoginCommand( @NotBlank
                                         @MobileOrEmail
                                         String mobileOrEmail,
                                         @NotBlank
                                         @Password
                                         String password ) implements Command {
    @Override
    public void correctAndValidate( ) {

    }
}
