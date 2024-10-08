package io.github.yeahfo.fit.core.login;

import io.github.yeahfo.fit.common.security.IPCookieUpdater;
import io.github.yeahfo.fit.common.security.CookieFactory;
import io.github.yeahfo.fit.core.common.domain.user.User;
import io.github.yeahfo.fit.core.login.application.TokenResponse;
import io.github.yeahfo.fit.core.login.application.LoginCommandService;
import io.github.yeahfo.fit.core.login.application.MobileOrEmailLoginCommand;
import io.github.yeahfo.fit.core.login.application.VerificationCodeLoginCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static io.github.yeahfo.fit.common.swagger.SwaggerConfiguration.AUTHORIZATION_BEARER_TOKEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@Tag( name = "Login", description = "Login APIs" )
public class LoginResource {
    private final CookieFactory cookieFactory;
    private final IPCookieUpdater cookieUpdater;
    private final LoginCommandService loginCommandService;

    @Operation( summary = "Login with mobile or email." )
    @PostMapping( value = "/login", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE )
    public TokenResponse loginWithMobileOrEmail( HttpServletRequest request,
                                                 HttpServletResponse response,
                                                 @Valid @RequestBody MobileOrEmailLoginCommand command ) {
        String token = loginCommandService.loginWithMobileOrEmail( command );
        Cookie cookie = cookieFactory.newJwtCookie( token );
        Cookie updatedCookie = cookieUpdater.updateCookie( cookie, request );
        response.addCookie( updatedCookie );
        return TokenResponse.builder( ).token( token ).build( );
    }

    @Operation( summary = "Login with verification code." )
    @PostMapping( value = "/verification-code-login", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE )
    public TokenResponse loginWithVerificationCode( HttpServletRequest request,
                                                    HttpServletResponse response,
                                                    @RequestBody @Valid VerificationCodeLoginCommand command ) {
        String jwt = loginCommandService.loginWithVerificationCode( command );
        response.addCookie( cookieUpdater.updateCookie( cookieFactory.newJwtCookie( jwt ), request ) );
        return TokenResponse.builder( ).token( jwt ).build( );
    }

    @Operation( summary = "Log out." )
    @DeleteMapping( value = "/logout" )
    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    public void logout( HttpServletRequest request,
                        HttpServletResponse response,
                        @AuthenticationPrincipal User user ) {
        response.addCookie( cookieUpdater.updateCookie( cookieFactory.logoutCookie( ), request ) );
        if ( user.isLoggedIn( ) ) {
            log.info( "User[{}] tried log out.", user.memberId( ) );
        }
    }

    @Operation( summary = "Refresh access token." )
    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @PutMapping( value = "/refresh-token", produces = APPLICATION_JSON_VALUE )
    public TokenResponse refreshToken( HttpServletRequest request,
                                       HttpServletResponse response,
                                       @AuthenticationPrincipal User user ) {
        String jwt = loginCommandService.refreshToken( user );
        response.addCookie( cookieUpdater.updateCookie( cookieFactory.newJwtCookie( jwt ), request ) );
        return TokenResponse.builder( ).token( jwt ).build( );
    }
}
