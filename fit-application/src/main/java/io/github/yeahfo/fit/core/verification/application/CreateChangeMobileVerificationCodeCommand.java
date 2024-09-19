package io.github.yeahfo.fit.core.verification.application;

import io.github.yeahfo.fit.core.common.application.Command;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import static io.github.yeahfo.fit.core.common.utils.FitRegexConstants.MOBILE_PATTERN;

@Builder
public record CreateChangeMobileVerificationCodeCommand( @NotBlank
                                                         @Pattern( regexp = MOBILE_PATTERN )
                                                         @Size( max = 20 )
                                                         String mobile ) implements Command {
    @Override
    public void correctAndValidate( ) {

    }
}
