package io.github.yeahfo.fit.core.member.application;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.github.yeahfo.fit.common.ratelimit.RateLimiter;
import io.github.yeahfo.fit.core.common.domain.user.User;
import io.github.yeahfo.fit.core.common.exception.FitException;
import io.github.yeahfo.fit.core.member.application.commands.*;
import io.github.yeahfo.fit.core.member.application.imports.MemberImporter;
import io.github.yeahfo.fit.core.member.application.queries.MemberImportResponse;
import io.github.yeahfo.fit.core.member.domain.Member;
import io.github.yeahfo.fit.core.member.domain.MemberDomainService;
import io.github.yeahfo.fit.core.member.domain.MemberRepository;
import io.github.yeahfo.fit.core.member.domain.events.MemberDomainEvent;
import io.github.yeahfo.fit.core.member.domain.events.MemberDomainEventPublisher;
import io.github.yeahfo.fit.core.tenant.domain.PackagesStatus;
import io.github.yeahfo.fit.core.tenant.domain.TenantRepository;
import io.github.yeahfo.fit.core.verification.domain.VerificationCodeChecker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;

import static io.github.yeahfo.fit.core.common.domain.user.Role.TENANT_ADMIN;
import static io.github.yeahfo.fit.core.common.domain.user.Role.TENANT_MEMBER;
import static io.github.yeahfo.fit.core.common.exception.ErrorCode.MEMBER_NOT_FOUND_FOR_FINDBACK_PASSWORD;
import static io.github.yeahfo.fit.core.common.utils.MapUtils.mapOf;
import static io.github.yeahfo.fit.core.verification.domain.VerificationCodeType.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberCommandService {
    private final RateLimiter rateLimiter;
    private final MemberImporter memberImporter;
    private final MemberRepository memberRepository;
    private final TenantRepository tenantRepository;
    private final MemberDomainService memberDomainService;
    private final VerificationCodeChecker verificationCodeChecker;
    private final MemberDomainEventPublisher domainEventPublisher;

    @Transactional
    public void registerMember( String mobile, String email, String password, User user ) {
        ResultWithDomainEvents< Member, MemberDomainEvent > resultWithDomainEvents = memberDomainService.register( mobile, email, password, user );
        Member member = memberRepository.save( resultWithDomainEvents.result );
        domainEventPublisher.publish( member, resultWithDomainEvents.events );
    }

    @Transactional
    public String createMember( CreateMemberCommand command, User user ) {
        user.checkIsTenantAdmin( );
        String tenantId = user.tenantId( );
        rateLimiter.applyFor( tenantId, "Member:Create", 5 );

        PackagesStatus packagesStatus = tenantRepository.packagesStatusOf( tenantId );
        packagesStatus.validateAddMember( );

        ResultWithDomainEvents< Member, MemberDomainEvent > resultWithDomainEvents = memberDomainService.create(
                command.name( ),
                command.departmentIds( ),
                command.mobile( ),
                command.email( ),
                command.password( ),
                command.customId( ),
                user
        );
        Member member = resultWithDomainEvents.result;
        memberRepository.save( member );
        domainEventPublisher.publish( member, resultWithDomainEvents.events );
        log.info( "Created member[{}].", member.identifier( ) );
        return member.identifier( );
    }

    public MemberImportResponse importMembers( InputStream inputStream, User user ) {
        user.checkIsTenantAdmin( );
        String tenantId = user.tenantId( );
        rateLimiter.applyFor( tenantId, "Member:Import", 1 );

        PackagesStatus packagesStatus = tenantRepository.packagesStatusOf( tenantId );
        packagesStatus.validateImportMember( );
        long remainingCount = packagesStatus.validateImportMembers( );

        MemberImportResponse response = memberImporter.importMembers( inputStream, remainingCount, user );
        log.info( "Imported {} members for tenant[{}].", response.importedCount( ), tenantId );
        return response;
    }

    @Transactional
    public void updateMember( String memberId, UpdateMemberInfoCommand command, User user ) {
        user.checkIsTenantAdmin( );
        String tenantId = user.tenantId( );
        rateLimiter.applyFor( tenantId, "Member:Update", 5 );

        Member member = memberRepository.findPresent( memberId );

        ResultWithDomainEvents< Member, MemberDomainEvent > resultWithDomainEvents = memberDomainService.updateMember( member,
                command.name( ),
                command.departmentIds( ),
                command.mobile( ),
                command.email( ),
                user );
        memberRepository.save( member );
        this.domainEventPublisher.publish( member, resultWithDomainEvents.events );
        log.info( "Updated detail for member[{}].", memberId );
    }

    @Transactional
    public void updateMemberRole( String memberId, UpdateMemberRoleCommand command, User user ) {
        user.checkIsTenantAdmin( );
        String tenantId = user.tenantId( );
        rateLimiter.applyFor( tenantId, "Member:UpdateRole", 5 );
        Member member = memberRepository.findPresent( memberId );
        if ( command.role( ) == member.role( ) ) {
            return;
        }

        member.updateRole( command.role( ), user );
        memberRepository.save( member );

        if ( command.role( ) == TENANT_MEMBER ) {
            memberDomainService.checkMinTenantAdminLimit( tenantId );
        }

        if ( command.role( ) == TENANT_ADMIN ) {
            memberDomainService.checkMaxTenantAdminLimit( tenantId );
        }

        log.info( "Updated member[{}] role to {}.", memberId, command.role( ) );
    }

    @Transactional
    public void deleteMember( String memberId, User user ) {
        user.checkIsTenantAdmin( );
        String tenantId = user.tenantId( );
        rateLimiter.applyFor( tenantId, "Member:Delete", 5 );

        Member member = memberRepository.findPresent( memberId );
        ResultWithDomainEvents< Member, MemberDomainEvent > resultWithDomainEvents = member.delete( user );
        memberDomainService.checkMinTenantAdminLimit( tenantId );
        memberRepository.delete( member );
        domainEventPublisher.publish( member, resultWithDomainEvents.events );
        log.info( "Deleted member[{}].", memberId );
    }

    @Transactional
    public void activateMember( String memberId, User user ) {
        user.checkIsTenantAdmin( );
        String tenantId = user.tenantId( );
        rateLimiter.applyFor( tenantId, "Member:Activate", 5 );

        Member member = memberRepository.findPresent( memberId );
        member.activate( user );
        memberRepository.save( member );
        log.info( "Activated member[{}].", memberId );
    }

    @Transactional
    public void deactivateMember( String memberId, User user ) {
        user.checkIsTenantAdmin( );
        String tenantId = user.tenantId( );
        rateLimiter.applyFor( tenantId, "Member:Deactivate", 5 );

        Member member = memberRepository.findPresent( memberId );
        member.deactivate( user );
        memberRepository.save( member );
        memberDomainService.checkMinTenantAdminLimit( member.tenantId( ) );
        log.info( "Deactivated member[{}].", memberId );
    }

    @Transactional
    public void resetPasswordForMember( String memberId, ResetMemberPasswordCommand command, User user ) {
        user.checkIsTenantAdmin( );
        rateLimiter.applyFor( user.tenantId( ), "Member:ResetPassword", 5 );

        Member member = memberRepository.findPresent( memberId );
        memberDomainService.resetPasswordForMember( member, command.password( ), user );
        memberRepository.save( member );
        log.info( "Reset password for member[{}].", memberId );
    }

    @Transactional
    public void changeMyPassword( ChangeMyPasswordCommand command, User user ) {
        rateLimiter.applyFor( user.tenantId( ), "Member:ChangeMyPassword", 5 );

        Member member = memberRepository.findPresent( user.memberId( ) );
        memberDomainService.changeMyPassword( member, command.oldPassword( ), command.newPassword( ) );
        memberRepository.save( member );
        log.info( "Password changed by member[{}].", member.identifier( ) );
    }

    @Transactional
    public void changeMyMobile( ChangeMyMobileCommand command, User user ) {
        rateLimiter.applyFor( user.tenantId( ), "Member:ChangeMyMobile", 5 );

        String mobile = command.mobile( );
        verificationCodeChecker.check( mobile, command.verification( ), CHANGE_MOBILE );

        Member member = memberRepository.findPresent( user.memberId( ) );
        memberDomainService.changeMyMobile( member, mobile, command.password( ) );
        memberRepository.save( member );
        log.info( "Mobile changed by member[{}].", member.identifier( ) );
    }

    @Transactional
    public void identifyMyMobile( IdentifyMyMobileCommand command, User user ) {
        rateLimiter.applyFor( user.tenantId( ), "Member:IdentifyMobile", 5 );

        String mobile = command.mobile( );
        verificationCodeChecker.check( mobile, command.verification( ), IDENTIFY_MOBILE );

        Member member = memberRepository.findPresent( user.memberId( ) );
        memberDomainService.identifyMyMobile( member, mobile );
        memberRepository.save( member );
        log.info( "Mobile identified by member[{}].", member.identifier( ) );
    }

    @Transactional
    public void updateMyBaseSetting( UpdateMyBaseSettingCommand command, User user ) {
        rateLimiter.applyFor( user.tenantId( ), "Member:UpdateMySetting", 5 );

        Member member = memberRepository.findPresent( user.memberId( ) );
        ResultWithDomainEvents< Member, MemberDomainEvent > resultWithDomainEvents = member.updateBaseSetting(
                command.name( ), user );
        domainEventPublisher.publish( member, resultWithDomainEvents.events );
        memberRepository.save( member );
        log.info( "Member base setting updated by member[{}].", member.identifier( ) );
    }

    @Transactional
    public void updateMyAvatar( UpdateMyAvatarCommand command, User user ) {
        rateLimiter.applyFor( user.tenantId( ), "Member:UpdateMyAvatar", 5 );

        Member member = memberRepository.findPresent( user.memberId( ) );
        member.updateAvatar( command.avatar( ), user );
        memberRepository.save( member );
        log.info( "Avatar updated by member[{}].", member.identifier( ) );
    }

    @Transactional
    public void deleteMyAvatar( User user ) {
        rateLimiter.applyFor( user.tenantId( ), "Member:DeleteMyAvatar", 5 );

        Member member = memberRepository.findPresent( user.memberId( ) );
        member.deleteAvatar( user );
        memberRepository.save( member );
        log.info( "Avatar deleted by member[{}].", member.identifier( ) );
    }

    @Transactional
    public void findBackPassword( FindBackPasswordCommand command ) {
        rateLimiter.applyFor( "Member:FindBackPassword:All", 5 );
        String mobileOrEmail = command.mobileOrEmail( );
        verificationCodeChecker.check( mobileOrEmail, command.verification( ), FIND_BACK_PASSWORD );

        Member member = memberRepository.findByMobileOrEmail( mobileOrEmail )
                .orElseThrow( ( ) -> new FitException( MEMBER_NOT_FOUND_FOR_FINDBACK_PASSWORD,
                        "没有找到手机号或密码对应用户。",
                        mapOf( "mobileOrEmail", mobileOrEmail ) ) );
        memberDomainService.resetPasswordForMember( member, command.password( ), member.toUser( ) );
        memberRepository.save( member );
        log.info( "Password found back by member[{}].", member.identifier( ) );
    }

    @Transactional
    public void topApp( String appId, User user ) {
        rateLimiter.applyFor( user.tenantId( ), "Member:TopApp", 5 );

        Member member = memberRepository.findPresent( user.memberId( ) );
        member.topApp( appId, user );
        memberRepository.save( member );
        log.info( "Mark app[{}] as top by member[{}].", appId, member.identifier( ) );
    }

    @Transactional
    public void cancelTopApp( String appId, User user ) {
        rateLimiter.applyFor( user.tenantId( ), "Member:CancelTopApp", 5 );

        Member member = memberRepository.findPresent( user.memberId( ) );
        member.cancelTopApp( appId, user );
        memberRepository.save( member );
        log.info( "Unmark app[{}] as top by member[{}].", appId, member.identifier( ) );
    }
}
