package io.github.yeahfo.fit.core.tenant;

import io.github.yeahfo.fit.core.common.domain.user.User;
import io.github.yeahfo.fit.core.tenant.application.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static io.github.yeahfo.fit.common.swagger.SwaggerConfiguration.AUTHORIZATION_BEARER_TOKEN;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping( "/tenants" )
@Tag( name = "Tenant", description = "Tenant APIs" )
public class TenantResource {
    private final TenantQueryService tenantQueryService;
    private final TenantCommandService tenantCommandService;


    @ResponseStatus( NO_CONTENT )
    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @PutMapping( value = "/current/base-setting", consumes = APPLICATION_JSON_VALUE )
    public void updateTenantBaseSetting( @RequestBody @Valid UpdateTenantBaseSettingCommand command,
                                         @AuthenticationPrincipal User user ) {
        tenantCommandService.updateTenantBaseSetting( command, user );
    }

    @ResponseStatus( NO_CONTENT )
    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @PutMapping( value = "/current/logo", consumes = APPLICATION_JSON_VALUE )
    public void updateTenantLogo( @RequestBody @Valid UpdateTenantLogoCommand command,
                                  @AuthenticationPrincipal User user ) {
        tenantCommandService.updateTenantLogo( command, user );
    }

    @ResponseStatus( NO_CONTENT )
    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @PutMapping( value = "/current/subdomain", consumes = APPLICATION_JSON_VALUE )
    public void updateTenantSubdomain( @RequestBody @Valid UpdateTenantSubdomainCommand command,
                                       @AuthenticationPrincipal User user ) {
        tenantCommandService.updateTenantSubdomain( command, user );
    }
}
