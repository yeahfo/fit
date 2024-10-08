package io.github.yeahfo.fit.common.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static org.springframework.security.config.Elements.JWT;

@Configuration
@ConditionalOnProperty( name = "springdoc.api-docs.enabled", matchIfMissing = true )
public class SwaggerConfiguration {
    public static final String AUTHORIZATION_BEARER_TOKEN = "AuthorizationBearerToken";
    public static final String STRING = "string";
    public static final Schema< ? > TOKEN_SCHEMA = new Schema<>( )
            .required( List.of( "token" ) )
            .name( "TokenResponse" )
            .title( "TokenResponse" )
            .description( "token information" )
            .addProperty( "token", new Schema<>( ).description( "Access token value" ).type( STRING ) );

    @Bean
    OpenAPI openAPI( ) {
        SecurityScheme TOKEN_SECURITY_SCHEME = new SecurityScheme( )
                .name( AUTHORIZATION_BEARER_TOKEN )
                .type( SecurityScheme.Type.HTTP )
                .scheme( "bearer" )
                .bearerFormat( JWT )
                .description( """
                        *Carry 'HTTP' request header 'Authorization' : 'Bearer' 'TOKEN_VALUE'*
                        ##### e.g:
                        ```shell
                        POST /api/v1/users HTTP/1.1 \s
                        Host: api.server.com
                        Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5...
                        ```
                        ###### Online debugging Paste Token values directly in the following input box (without the 'Bearer' prefix)
                        """ );
        return new OpenAPI( ).info( new Info( )
                        .title( "FIT OpenAPI Document" )
                        .version( "0.0.1" )
                        .description( "Provide FIT content service" )
                        .contact( new Contact( ).name( "FIT" ).url( "https://github.com/yeahfo/fit" )
                                .email( "fit@qq.com" ) ) )
                .components( new Components( )
                                .addSecuritySchemes( TOKEN_SECURITY_SCHEME.getName( ), TOKEN_SECURITY_SCHEME )
                                .addSchemas( TOKEN_SCHEMA.getName( ), TOKEN_SCHEMA )
//                        .addSchemas( ROLE_SCHEMA.getName( ), ROLE_SCHEMA )
                );
    }

    @Bean
    public OpenApiCustomizer sortSchemasAlphabetically( ) {
        return api -> {
            Paths paths = new Paths( );
            api.getPaths( ).entrySet( )
                    .stream( )
                    .sorted( Comparator.comparing( entry -> entry.getKey( ).length( ) ) )
                    .collect( toMap( Map.Entry::getKey, Map.Entry::getValue, ( x, y ) -> y, LinkedHashMap::new ) )
                    .forEach( paths::addPathItem );
            api.setPaths( paths );
        };
    }
}
