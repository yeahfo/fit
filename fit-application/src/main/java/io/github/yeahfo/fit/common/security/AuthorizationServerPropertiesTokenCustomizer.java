package io.github.yeahfo.fit.common.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.Duration;

@ConfigurationProperties( "spring.security.oauth2.authorizationserver.token.customizer" )
public record AuthorizationServerPropertiesTokenCustomizer( @DefaultValue( DEFAULT_ISSUER ) String issuer,
                                                            @DefaultValue( DEFAULT_SECRET ) String secret,
                                                            @DefaultValue( "60m" ) Duration expiration,
                                                            @DefaultValue( "1m" ) Duration aheadAutoRefresh ) {
    static final String DEFAULT_ISSUER = "FIT";
    static final String DEFAULT_SECRET = "1234567890_1234567890_1234567890";
}
