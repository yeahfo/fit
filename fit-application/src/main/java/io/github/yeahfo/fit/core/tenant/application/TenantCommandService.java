package io.github.yeahfo.fit.core.tenant.application;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.github.yeahfo.fit.common.ratelimit.RateLimiter;
import io.github.yeahfo.fit.core.common.domain.user.User;
import io.github.yeahfo.fit.core.member.domain.MemberRepository;
import io.github.yeahfo.fit.core.plan.domain.PlanType;
import io.github.yeahfo.fit.core.tenant.domain.Tenant;
import io.github.yeahfo.fit.core.tenant.domain.TenantRepository;
import io.github.yeahfo.fit.core.tenant.domain.events.TenantDomainEvent;
import io.github.yeahfo.fit.core.tenant.domain.events.TenantDomainEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static io.github.yeahfo.fit.core.common.domain.user.User.NOUSER;

@Slf4j
@Service
@RequiredArgsConstructor
public class TenantCommandService {
    private final RateLimiter rateLimiter;
    private final TenantRepository tenantRepository;
    private final MemberRepository memberRepository;
    private final TenantDomainEventPublisher domainEventPublisher;

    @Transactional
    public void createTenant( CreateTenantCommand command ) {
        ResultWithDomainEvents< Tenant, TenantDomainEvent > resultWithDomainEvents = Tenant.create( command.name( ), command.user( ) );
        Tenant tenant = tenantRepository.save( resultWithDomainEvents.result );
        domainEventPublisher.publish( tenant, resultWithDomainEvents.events );
    }

    public boolean exists( String id ) {
        return tenantRepository.existsById( id );
    }

    @Transactional
    public void updateTenantPlanType( String tenantId, PlanType planType, Instant expireAt, User user ) {
        rateLimiter.applyFor( tenantId, "Tenant:UpdatePlanType", 5 );
        Tenant tenant = tenantRepository.find( tenantId );
        ResultWithDomainEvents< Tenant, TenantDomainEvent > resultEvents = tenant.updatePlanType( planType, expireAt, user );
        tenantRepository.save( tenant );
        domainEventPublisher.publish( tenant, resultEvents.events );
    }

    @Transactional
    public void countTenantMembers( String id ) {
        tenantRepository.findById( id ).ifPresent( tenant -> {
            long count = memberRepository.countByTenantId( id );
            ResultWithDomainEvents< Tenant, TenantDomainEvent > domainEvents = tenant.updateMemberCount( count, NOUSER );
            tenantRepository.save( tenant );
            domainEventPublisher.publish( tenant, domainEvents.events );
            log.info( "Counted all {} members for tenant[{}].", count, id );
        } );
    }
}
