package io.github.yeahfo.fit.core.register.application;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.eventuate.tram.events.common.DomainEvent;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.github.yeahfo.fit.common.ratelimit.RateLimiter;
import io.github.yeahfo.fit.core.common.domain.AggregateRoot;
import io.github.yeahfo.fit.core.common.domain.user.User;
import io.github.yeahfo.fit.core.departmenthierarchy.domain.DepartmentHierarchy;
import io.github.yeahfo.fit.core.departmenthierarchy.domain.DepartmentHierarchyRepository;
import io.github.yeahfo.fit.core.member.domain.Member;
import io.github.yeahfo.fit.core.member.domain.MemberRepository;
import io.github.yeahfo.fit.core.register.domain.RegisterDomainService;
import io.github.yeahfo.fit.core.tenant.domain.CreateTenantHolder;
import io.github.yeahfo.fit.core.tenant.domain.Tenant;
import io.github.yeahfo.fit.core.tenant.domain.TenantRepository;
import io.github.yeahfo.fit.core.verification.domain.VerificationCodeChecker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static io.github.yeahfo.fit.core.common.domain.user.Role.TENANT_ADMIN;
import static io.github.yeahfo.fit.core.common.utils.Identified.newMemberId;
import static io.github.yeahfo.fit.core.common.utils.Identified.newTenantId;
import static io.github.yeahfo.fit.core.verification.domain.VerificationCodeType.REGISTER;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterCommandService {
    private final RateLimiter rateLimiter;
    private final MemberRepository memberRepository;
    private final TenantRepository tenantRepository;
    private final DomainEventPublisher domainEventPublisher;
    private final RegisterDomainService registerDomainService;
    private final VerificationCodeChecker verificationCodeChecker;
    private final DepartmentHierarchyRepository departmentHierarchyRepository;

    @Transactional
    public RegisteredResponse register( RegisterCommand command ) {
        rateLimiter.applyFor( "Registration:Register:All", 20 );

        String mobileOrEmail = command.mobileOrEmail( );
        verificationCodeChecker.check( mobileOrEmail, command.verification( ), REGISTER );

        User user = User.humanUser( newMemberId( ), command.username( ), newTenantId( ), TENANT_ADMIN );
        CreateTenantHolder holder = registerDomainService.register( mobileOrEmail, command.password( ), command.tenantName( ), user,
                this::publishDomainEvents, this::publishDomainEvents, this::publishDomainEvents
        );

        Tenant tenant = holder.tenant( );
        Member member = holder.member( );
        DepartmentHierarchy departmentHierarchy = holder.departmentHierarchy( );

        tenantRepository.save( tenant );
        memberRepository.save( member );
        departmentHierarchyRepository.save( departmentHierarchy );
        log.info( "Registered tenant[{}] with admin member[{}].", tenant.identifier( ), member.identifier( ) );

        return RegisteredResponse.builder( ).tenantId( tenant.identifier( ) ).memberId( member.identifier( ) ).build( );
    }

    private < A extends AggregateRoot, E extends DomainEvent > void publishDomainEvents( ResultWithDomainEvents< A, E > resultEvents ) {
        domainEventPublisher.publish( resultEvents.result.getClass( ), resultEvents.result.identifier( ), resultEvents
                .events.stream( ).map( event -> ( DomainEvent ) event ).toList( ) );
    }

}
