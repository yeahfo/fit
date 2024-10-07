package io.github.yeahfo.fit.core.login;

import io.github.yeahfo.fit.common.security.IPCookieUpdater;
import io.github.yeahfo.fit.common.security.CookieFactory;
import io.github.yeahfo.fit.core.login.application.TokenResponse;
import io.github.yeahfo.fit.core.login.application.LoginCommandService;
import io.github.yeahfo.fit.core.login.application.MobileOrEmailLoginCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
