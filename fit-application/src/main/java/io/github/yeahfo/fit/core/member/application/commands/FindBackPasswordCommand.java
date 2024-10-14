package io.github.yeahfo.fit.core.member.application.commands;

import io.github.yeahfo.fit.core.common.application.Command;
import io.github.yeahfo.fit.core.common.validation.mobileoremail.MobileOrEmail;
import io.github.yeahfo.fit.core.common.validation.password.Password;
import io.github.yeahfo.fit.core.common.validation.verficationcode.VerificationCode;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record FindBackPasswordCommand( @NotBlank
                                       @MobileOrEmail
                                       String mobileOrEmail,

                                       @NotBlank
                                       @VerificationCode
                                       String verification,

                                       @NotBlank
                                       @Password
                                       String password ) implements Command {
    @Override
    public void correctAndValidate( ) {

    }
}
