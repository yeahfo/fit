package io.github.yeahfo.fit.core.verification.application;

import io.github.yeahfo.fit.core.common.application.Command;
import io.github.yeahfo.fit.core.common.validation.mobileoremail.MobileOrEmail;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CreateFindBackPasswordVerificationCodeCommand( @NotBlank
                                                             @MobileOrEmail
                                                             String mobileOrEmail ) implements Command {
    @Override
    public void correctAndValidate( ) {

    }
}
