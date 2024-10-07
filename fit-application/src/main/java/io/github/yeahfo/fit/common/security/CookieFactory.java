package io.github.yeahfo.fit.common.security;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;

import static io.github.yeahfo.fit.core.common.utils.FitConstants.AUTH_COOKIE_NAME;
import static io.github.yeahfo.fit.core.common.utils.FitConstants.Profiles.prod;
import static io.github.yeahfo.fit.core.common.utils.FitConstants.Profiles.production;
import static java.util.Arrays.asList;

@Component
@RequiredArgsConstructor
public class CookieFactory {
    private final Environment environment;
    private final AuthorizationServerPropertiesTokenCustomizer properties;


    public Cookie newJwtCookie( String cookieValue ) {
        List< String > activeProfiles = asList( environment.getActiveProfiles( ) );
        if ( activeProfiles.contains( prod ) || activeProfiles.contains( production ) ) {
            return newProdCookie( cookieValue );
        }
        return newNonProdCookie( cookieValue );
    }

    private Cookie newNonProdCookie( String cookieValue ) {
        Cookie cookie = new Cookie( AUTH_COOKIE_NAME, cookieValue );
        cookie.setMaxAge( ( int ) properties.expiration( ).getSeconds( ) );
        cookie.setPath( "/" );
        return cookie;
    }

    private Cookie newProdCookie( String cookieValue ) {
        Cookie cookie = new Cookie( AUTH_COOKIE_NAME, cookieValue );
        cookie.setMaxAge( ( int ) properties.expiration( ).getSeconds( ) );
        cookie.setPath( "/" );
        cookie.setSecure( true );
        cookie.setHttpOnly( true );
        return cookie;
    }

    public Cookie logoutCookie( ) {
        Cookie cookie = new Cookie( AUTH_COOKIE_NAME, "" );
        cookie.setMaxAge( 0 );
        cookie.setPath( "/" );
        return cookie;
    }

}
