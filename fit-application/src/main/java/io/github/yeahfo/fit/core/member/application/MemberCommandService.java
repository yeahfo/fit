package io.github.yeahfo.fit.core.member.application;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.github.yeahfo.fit.common.ratelimit.RateLimiter;
import io.github.yeahfo.fit.core.common.domain.user.User;
import io.github.yeahfo.fit.core.member.domain.Member;
import io.github.yeahfo.fit.core.member.domain.MemberDomainService;
import io.github.yeahfo.fit.core.member.domain.MemberRepository;
import io.github.yeahfo.fit.core.member.domain.events.MemberDomainEvent;
import io.github.yeahfo.fit.core.member.domain.events.MemberDomainEventPublisher;
import io.github.yeahfo.fit.core.tenant.domain.PackagesStatus;
import io.github.yeahfo.fit.core.tenant.domain.TenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberCommandService {
    private final RateLimiter rateLimiter;
    private final MemberImporter memberImporter;
    private final MemberRepository memberRepository;
    private final TenantRepository tenantRepository;
    private final MemberDomainService memberDomainService;
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
}
