package io.github.yeahfo.fit.core.member;

import io.github.yeahfo.fit.core.common.application.IdentifierResponse;
import io.github.yeahfo.fit.core.common.application.PagedResponse;
import io.github.yeahfo.fit.core.common.domain.user.User;
import io.github.yeahfo.fit.core.common.validation.id.app.AppId;
import io.github.yeahfo.fit.core.common.validation.id.member.MemberId;
import io.github.yeahfo.fit.core.common.validation.id.tenant.TenantId;
import io.github.yeahfo.fit.core.member.application.*;
import io.github.yeahfo.fit.core.member.application.commands.*;
import io.github.yeahfo.fit.core.member.application.queries.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static io.github.yeahfo.fit.common.swagger.SwaggerConfiguration.AUTHORIZATION_BEARER_TOKEN;
import static io.github.yeahfo.fit.core.common.application.IdentifierResponse.identifier;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping( "/members" )
@Tag( name = "Member", description = "Member APIs" )
public class MemberResource {

    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;


    @ResponseStatus( CREATED )
    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @PostMapping( consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE )
    public IdentifierResponse createMember( @RequestBody @Valid CreateMemberCommand command,
                                            @AuthenticationPrincipal User user ) {
        String memberId = memberCommandService.createMember( command, user );
        return identifier( memberId );
    }

    @ResponseStatus( CREATED )
    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @PostMapping( value = "/import", consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE )
    public MemberImportResponse importMembers( @RequestParam @NotNull MultipartFile file,
                                               @AuthenticationPrincipal User user ) throws IOException {
        return memberCommandService.importMembers( file.getInputStream( ), user );
    }

    @ResponseStatus( ACCEPTED )
    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @PutMapping( value = "/{memberId}", consumes = APPLICATION_JSON_VALUE )
    public void updateMember( @PathVariable @NotBlank @MemberId String memberId,
                              @RequestBody @Valid UpdateMemberInfoCommand command,
                              @AuthenticationPrincipal User user ) {
        memberCommandService.updateMember( memberId, command, user );
    }

    @ResponseStatus( ACCEPTED )
    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @PutMapping( value = "/{memberId}/role", consumes = APPLICATION_JSON_VALUE )
    public void updateMemberRole( @PathVariable @NotBlank @MemberId String memberId,
                                  @RequestBody @Valid UpdateMemberRoleCommand command,
                                  @AuthenticationPrincipal User user ) {
        memberCommandService.updateMemberRole( memberId, command, user );
    }

    @ResponseStatus( ACCEPTED )
    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @DeleteMapping( value = "/{memberId}" )
    public void deleteMember( @PathVariable @NotBlank @MemberId String memberId,
                              @AuthenticationPrincipal User user ) {
        memberCommandService.deleteMember( memberId, user );
    }

    @ResponseStatus( ACCEPTED )
    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @PutMapping( value = "/{memberId}/activation" )
    public void activateMember( @PathVariable( "memberId" ) @NotBlank @MemberId String memberId,
                                @AuthenticationPrincipal User user ) {
        memberCommandService.activateMember( memberId, user );
    }

    @ResponseStatus( ACCEPTED )
    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @PutMapping( value = "/{memberId}/deactivation" )
    public void deactivateMember( @PathVariable( "memberId" ) @NotBlank @MemberId String memberId,
                                  @AuthenticationPrincipal User user ) {
        memberCommandService.deactivateMember( memberId, user );
    }

    @ResponseStatus( ACCEPTED )
    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @PutMapping( value = "/{memberId}/password", consumes = APPLICATION_JSON_VALUE )
    public void resetPasswordForMember( @PathVariable( "memberId" ) @NotBlank @MemberId String memberId,
                                        @RequestBody @Valid ResetMemberPasswordCommand command,
                                        @AuthenticationPrincipal User user ) {
        memberCommandService.resetPasswordForMember( memberId, command, user );
    }

    @ResponseStatus( ACCEPTED )
    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @PutMapping( value = "/me/password", consumes = APPLICATION_JSON_VALUE )
    public void changeMyPassword( @RequestBody @Valid ChangeMyPasswordCommand command,
                                  @AuthenticationPrincipal User user ) {
        memberCommandService.changeMyPassword( command, user );
    }

    @ResponseStatus( ACCEPTED )
    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @PutMapping( value = "/me/mobile", consumes = APPLICATION_JSON_VALUE )
    public void changeMyMobile( @RequestBody @Valid ChangeMyMobileCommand command,
                                @AuthenticationPrincipal User user ) {
        memberCommandService.changeMyMobile( command, user );
    }

    @ResponseStatus( ACCEPTED )
    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @PutMapping( value = "/me/mobile-identification", consumes = APPLICATION_JSON_VALUE )
    public void identifyMyMobile( @RequestBody @Valid IdentifyMyMobileCommand command,
                                  @AuthenticationPrincipal User user ) {
        memberCommandService.identifyMyMobile( command, user );
    }

    @ResponseStatus( ACCEPTED )
    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @PutMapping( value = "/me/base-setting", consumes = APPLICATION_JSON_VALUE )
    public void updateMyBaseSetting( @RequestBody @Valid UpdateMyBaseSettingCommand command,
                                     @AuthenticationPrincipal User user ) {
        memberCommandService.updateMyBaseSetting( command, user );
    }

    @ResponseStatus( ACCEPTED )
    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @PutMapping( value = "/me/avatar", consumes = APPLICATION_JSON_VALUE )
    public void updateMyAvatar( @RequestBody @Valid UpdateMyAvatarCommand command,
                                @AuthenticationPrincipal User user ) {
        memberCommandService.updateMyAvatar( command, user );
    }

    @ResponseStatus( ACCEPTED )
    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @DeleteMapping( value = "/me/avatar" )
    public void deleteMyAvatar( @AuthenticationPrincipal User user ) {
        memberCommandService.deleteMyAvatar( user );
    }

    @ResponseStatus( ACCEPTED )
    @PostMapping( value = "/find-back-password", consumes = APPLICATION_JSON_VALUE )
    public void findBackPassword( @RequestBody @Valid FindBackPasswordCommand command ) {
        memberCommandService.findBackPassword( command );
    }

    @ResponseStatus( ACCEPTED )
    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @PutMapping( value = "/me/top-apps/{appId}" )
    public void topApp( @PathVariable( "appId" ) @NotBlank @AppId String appId,
                        @AuthenticationPrincipal User user ) {
        memberCommandService.topApp( appId, user );
    }

    @ResponseStatus( ACCEPTED )
    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @DeleteMapping( value = "/me/top-apps/{appId}" )
    public void cancelTopApp( @PathVariable( "appId" ) @NotBlank @AppId String appId,
                              @AuthenticationPrincipal User user ) {
        memberCommandService.cancelTopApp( appId, user );
    }

    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @GetMapping( value = "/me", produces = APPLICATION_JSON_VALUE )
    public ConsoleMemberProfile fetchMyProfile( @AuthenticationPrincipal User user ) {
        return memberQueryService.fetchMyProfile( user );
    }

    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @GetMapping( value = "/client/me", produces = APPLICATION_JSON_VALUE )
    public ClientMemberProfile fetchMyClientProfile( @AuthenticationPrincipal User user ) {
        return memberQueryService.fetchMyClientMemberProfile( user );
    }

    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @GetMapping( value = "/me/info", produces = APPLICATION_JSON_VALUE )
    public MemberInfo fetchMyMemberInfo( @AuthenticationPrincipal User user ) {
        return memberQueryService.fetchMyMemberInfo( user );
    }

    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @GetMapping( value = "/me/base-setting", produces = APPLICATION_JSON_VALUE )
    public MemberBaseSetting fetchMyBaseSetting( @AuthenticationPrincipal User user ) {
        return memberQueryService.fetchMyBaseSetting( user );
    }

    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @PostMapping( value = "/my-managed-members", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE )
    public PagedResponse< ListMember > listMyManagedMembers( @RequestBody @Valid ListMyManagedMembersCommand command,
                                                             @AuthenticationPrincipal User user ) {
        Page< ListMember > page = memberQueryService.listMyManagedMembers( command, user );
        return PagedResponse.< ListMember >builder( )
                .page( page.getNumber( ) + 1 )
                .size( page.getSize( ) )
                .total( page.getTotalElements( ) )
                .content( page.getContent( ) )
                .build( );
    }

    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @GetMapping( value = "/all-references", produces = APPLICATION_JSON_VALUE )
    public List< MemberReferenced > listMemberReferences( @AuthenticationPrincipal User user ) {
        return memberQueryService.listMemberReferences( user );
    }

    @SecurityRequirement( name = AUTHORIZATION_BEARER_TOKEN )
    @GetMapping( value = "/all-references/{tenantId}", produces = APPLICATION_JSON_VALUE )
    public List< MemberReferenced > listMemberReferencesForTenant( @PathVariable @NotBlank @TenantId String tenantId,
                                                                   @AuthenticationPrincipal User user ) {
        return memberQueryService.listMemberReferences( tenantId, user );
    }
}
