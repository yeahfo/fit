package io.github.yeahfo.fit.common.security.jwt;

import io.github.yeahfo.fit.common.security.AuthorizationServerPropertiesTokenCustomizer;
import io.github.yeahfo.fit.common.security.CookieFactory;
import io.github.yeahfo.fit.common.security.CustomizedAuthenticationToken;
import io.github.yeahfo.fit.common.security.IPCookieUpdater;
import io.github.yeahfo.fit.core.common.domain.user.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
public class JwtAutoRefreshFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final IPCookieUpdater ipCookieUpdater;
    private final CookieFactory cookieFactory;
    private final AuthorizationServerPropertiesTokenCustomizer properties;

    @Override
    protected void doFilterInternal( @NonNull HttpServletRequest request,
                                     @NonNull HttpServletResponse response,
                                     @NonNull FilterChain filterChain )
            throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext( ).getAuthentication( );
        if ( authentication instanceof CustomizedAuthenticationToken token && authentication.isAuthenticated( ) ) {
            User user = token.getUser( );
            if ( user.isHumanUser( ) ) {
                long timeLeft = token.getExpiration( ) - Instant.now( ).toEpochMilli( );
                if ( timeLeft > 0 && timeLeft < properties.aheadAutoRefresh( ).toMillis( ) ) {
                    String jwt = jwtService.generateJwt( user.memberId( ) );
                    Cookie cookie = cookieFactory.newJwtCookie( jwt );
                    response.addCookie( ipCookieUpdater.updateCookie( cookie, request ) );
                    //noinspection UastIncorrectHttpHeaderInspection
                    response.addHeader( "x-refreshed-token", jwt );
                    log.info( "Automatic token refresh with time left {} minutes to expire.", timeLeft / 60000 );
                }
            }
        }

        filterChain.doFilter( request, response );
    }
}
