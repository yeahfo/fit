package io.github.yeahfo.fit.core.tenant;

import io.github.yeahfo.fit.core.common.domain.user.User;
import io.github.yeahfo.fit.core.common.validation.nospace.NoSpace;
import io.github.yeahfo.fit.core.order.domain.delivery.Consignee;
import io.github.yeahfo.fit.core.tenant.application.*;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static io.github.yeahfo.fit.common.swagger.SwaggerConfiguration.AUTHORIZATION_BEARER_TOKEN;
import static io.github.yeahfo.fit.common.swagger.SwaggerConfiguration.STRING;
import static org.springframework.http.HttpStatus.*;
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

    @ResponseStatus( ACCEPTED )
    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @ApiResponse(
            content = @Content( mediaType = APPLICATION_JSON_VALUE, schemaProperties = {
                    @SchemaProperty( name = "secret", schema = @Schema( type = STRING, description = "Refreshed API secret value." ) )
            } ),
            responseCode = "202" )
    @PutMapping( value = "/current/api-secret" )
    public Map< String, String > refreshTenantApiSecret( @AuthenticationPrincipal User user ) {
        return Map.of( "secret", tenantCommandService.refreshTenantApiSecret( user ) );
    }

    @ResponseStatus( NO_CONTENT )
    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @PutMapping( value = "/current/invoice-title", consumes = APPLICATION_JSON_VALUE )
    public void updateTenantInvoiceTitle( @RequestBody @Valid UpdateTenantInvoiceTitleCommand command,
                                          @AuthenticationPrincipal User user ) {
        tenantCommandService.updateTenantInvoiceTitle( command, user );
    }

    @ResponseStatus( CREATED )
    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @PostMapping( value = "/current/consignees", consumes = APPLICATION_JSON_VALUE )
    public void addConsignee( @RequestBody @Valid AddConsigneeCommand command,
                              @AuthenticationPrincipal User user ) {
        tenantCommandService.addConsignee( command, user );
    }

    @ResponseStatus( NO_CONTENT )
    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @PutMapping( value = "/current/consignees", consumes = APPLICATION_JSON_VALUE )
    public void updateConsignee( @RequestBody @Valid UpdateConsigneeCommand command,
                                 @AuthenticationPrincipal User user ) {
        tenantCommandService.updateConsignee( command, user );
    }

    @ResponseStatus( NO_CONTENT )
    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @DeleteMapping( value = "/current/consignees/{consigneeId}" )
    public void deleteConsignee( @PathVariable( "consigneeId" ) @NotBlank String consigneeId,
                                 @AuthenticationPrincipal User user ) {
        tenantCommandService.deleteConsignee( consigneeId, user );
    }

    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @GetMapping( value = "/current/info", produces = APPLICATION_JSON_VALUE )
    public TenantInfo fetchTenantInfo( @AuthenticationPrincipal User user ) {
        return tenantQueryService.fetchTenantInfo( user );
    }

    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @GetMapping( value = "/current/base-setting", produces = APPLICATION_JSON_VALUE )
    public TenantBaseSetting fetchTenantBaseSetting( @AuthenticationPrincipal User user ) {
        return tenantQueryService.fetchTenantBaseSetting( user );
    }

    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @GetMapping( value = "/current/logo", produces = APPLICATION_JSON_VALUE )
    public TenantLogo fetchTenantLogo( @AuthenticationPrincipal User user ) {
        return tenantQueryService.fetchTenantLogo( user );
    }

    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @GetMapping( value = "/current/subdomain", produces = APPLICATION_JSON_VALUE )
    public TenantSubdomain fetchTenantSubdomain( @AuthenticationPrincipal User user ) {
        return tenantQueryService.fetchTenantSubdomain( user );
    }

    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @GetMapping( value = "/current/api-setting", produces = APPLICATION_JSON_VALUE )
    public TenantApiSetting fetchTenantApiSetting( @AuthenticationPrincipal User user ) {
        return tenantQueryService.fetchTenantApiSetting( user );
    }

    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @GetMapping( value = "/public-profile/{subdomainPrefix}", produces = APPLICATION_JSON_VALUE )
    public TenantPublicProfile fetchTenantPublicProfile( @PathVariable( "subdomainPrefix" )
                                                         @Size( max = 20 )
                                                         @NotBlank
                                                         @NoSpace String subdomainPrefix ) {
        return tenantQueryService.fetchTenantPublicProfile( subdomainPrefix );
    }

    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @GetMapping( value = "/current/invoice-title", produces = APPLICATION_JSON_VALUE )
    public TenantInvoiceTitle fetchTenantInvoiceTitle( @AuthenticationPrincipal User user ) {
        return tenantQueryService.fetchTenantInvoiceTitle( user );
    }

    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @GetMapping( value = "/current/consignees", produces = APPLICATION_JSON_VALUE )
    public List< Consignee > listTenantConsignees( @AuthenticationPrincipal User user ) {
        return tenantQueryService.listConsignees( user );
    }
}
