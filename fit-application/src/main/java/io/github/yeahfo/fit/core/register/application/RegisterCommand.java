package io.github.yeahfo.fit.core.register.application;

import io.github.yeahfo.fit.core.common.application.Command;
import io.github.yeahfo.fit.core.common.exception.FitException;
import io.github.yeahfo.fit.core.common.validation.mobileoremail.MobileOrEmail;
import io.github.yeahfo.fit.core.common.validation.password.Password;
import io.github.yeahfo.fit.core.common.validation.verficationcode.VerificationCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import static io.github.yeahfo.fit.core.common.exception.ErrorCode.MUST_SIGN_AGREEMENT;
import static io.github.yeahfo.fit.core.common.utils.FitConstants.MAX_GENERIC_NAME_LENGTH;

public record RegisterCommand( @NotBlank
                               @MobileOrEmail
                               String mobileOrEmail,
                               @NotBlank
                               @VerificationCode
                               String verification,
                               @NotBlank
                               @Password
                               String password,
                               @NotBlank
                               @Size( max = MAX_GENERIC_NAME_LENGTH )
                               String username,
                               @NotBlank
                               @Size( max = MAX_GENERIC_NAME_LENGTH )
                               String tenantName,
                               boolean agreement ) implements Command {
    @Override
    public void correctAndValidate( ) {
        if ( !agreement ) {
            throw new FitException( MUST_SIGN_AGREEMENT, "请同意用户协议以完成注册。" );
        }
    }
}
