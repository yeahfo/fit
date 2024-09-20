package io.github.yeahfo.fit.core.verification;

import io.github.yeahfo.fit.core.common.application.IdentifierResponse;
import io.github.yeahfo.fit.core.common.domain.user.User;
import io.github.yeahfo.fit.core.verification.application.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static io.github.yeahfo.fit.common.swagger.SwaggerConfiguration.AUTHORIZATION_BEARER_JWT;
import static io.github.yeahfo.fit.core.common.application.IdentifierResponse.identifier;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping( "/verification-codes" )
@Tag( name = "Verification code", description = "Create various types of verification codes" )
public class VerificationResource {
    private final VerificationCodeCommandService verificationCodeCommandService;

    @ResponseStatus( CREATED )
    @PostMapping( value = "/for-register", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE )
    public IdentifierResponse createVerificationCodeForRegister(
            @RequestBody @Valid CreateRegisterVerificationCodeCommand command ) {
        String verificationCodeId = verificationCodeCommandService.createVerificationCodeForRegister( command );
        return identifier( verificationCodeId );
    }

    @ResponseStatus( CREATED )
    @PostMapping( value = "/for-login",
            consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE )
    public IdentifierResponse createVerificationCodeForLogin(
            @RequestBody @Valid CreateLoginVerificationCodeCommand command ) {
        String verificationCodeId = verificationCodeCommandService.createVerificationCodeForLogin( command );
        return identifier( verificationCodeId );
    }

    @ResponseStatus( CREATED )
    @PostMapping( value = "/for-find-back-password", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE )
    public IdentifierResponse createVerificationCodeForFindBackPassword(
            @RequestBody @Valid CreateFindBackPasswordVerificationCodeCommand command ) {
        String verificationCodeId = verificationCodeCommandService.createVerificationCodeForFindBackPassword( command );
        return identifier( verificationCodeId );
    }

    @ResponseStatus( CREATED )
    @SecurityRequirement( name = AUTHORIZATION_BEARER_JWT )
    @PostMapping( value = "/for-change-mobile", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE )
    public IdentifierResponse createVerificationCodeForChangeMobile(
            @RequestBody @Valid CreateChangeMobileVerificationCodeCommand command,
            @AuthenticationPrincipal User user ) {
        String verificationCodeId = verificationCodeCommandService.createVerificationCodeForChangeMobile( command, user );
        return identifier( verificationCodeId );
    }

    @ResponseStatus( CREATED )
    @SecurityRequirement( name = AUTHORIZATION_BEARER_JWT )
    @PostMapping( value = "/for-identity-mobile", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE )
    public IdentifierResponse createVerificationCodeForIdentifyMobile(
            @RequestBody @Valid IdentifyMobileVerificationCodeCommand command,
            @AuthenticationPrincipal User user ) {
        String verificationCodeId = verificationCodeCommandService.createVerificationCodeForIdentifyMobile( command, user );
        return identifier( verificationCodeId );
    }
}
