package io.github.yeahfo.fit.core.member.application;

import io.github.yeahfo.fit.core.common.application.Command;
import io.github.yeahfo.fit.core.common.validation.mobile.Mobile;
import io.github.yeahfo.fit.core.common.validation.verficationcode.VerificationCode;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record IdentifyMyMobileCommand( @Mobile
                                       @NotBlank
                                       String mobile,

                                       @NotBlank
                                       @VerificationCode
                                       String verification ) implements Command {
    @Override
    public void correctAndValidate( ) {

    }
}
