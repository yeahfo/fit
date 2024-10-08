package io.github.yeahfo.fit.core.login.application;

import io.github.yeahfo.fit.core.common.application.Command;
import io.github.yeahfo.fit.core.common.validation.mobileoremail.MobileOrEmail;
import io.github.yeahfo.fit.core.common.validation.verficationcode.VerificationCode;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record VerificationCodeLoginCommand( @NotBlank
                                            @MobileOrEmail
                                            String mobileOrEmail,
                                            @NotBlank
                                            @VerificationCode
                                            String verification ) implements Command {
    @Override
    public void correctAndValidate( ) {

    }
}
