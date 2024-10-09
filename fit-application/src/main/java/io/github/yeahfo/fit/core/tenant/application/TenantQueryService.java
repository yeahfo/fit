package io.github.yeahfo.fit.core.tenant.application;

import io.github.yeahfo.fit.common.ratelimit.RateLimiter;
import io.github.yeahfo.fit.core.common.domain.user.User;
import io.github.yeahfo.fit.core.order.domain.delivery.Consignee;
import io.github.yeahfo.fit.core.plan.domain.Plan;
import io.github.yeahfo.fit.core.tenant.domain.Packages;
import io.github.yeahfo.fit.core.tenant.domain.ResourceUsage;
import io.github.yeahfo.fit.core.tenant.domain.Tenant;
import io.github.yeahfo.fit.core.tenant.domain.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.Math.max;
import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_UP;

@Service
@RequiredArgsConstructor
public class TenantQueryService {
    private final RateLimiter rateLimiter;
    private final TenantRepository tenantRepository;

    public TenantInfo fetchTenantInfo( User user ) {
        user.checkIsTenantAdmin( );
        String tenantId = user.tenantId( );
        rateLimiter.applyFor( tenantId, "Tenant:FetchInfo", 5 );

        Tenant tenant = tenantRepository.findPresent( tenantId );

        Packages packages = tenant.getPackages( );
        ResourceUsage resourceUsage = tenant.getResourceUsage( );
        Plan currentPlan = tenant.currentPlan( );

        return TenantInfo.builder( )
                .tenantId( tenant.identifier( ) )
                .name( tenant.getName( ) )
                .planType( tenant.currentPlanType( ) )
                .createdAt( tenant.createdAt( ) )
                .createdBy( tenant.createdBy( ) )
                .creator( tenant.creator( ) )
                .packagesName( currentPlan.name( ) )
                .planMaxAppCount( currentPlan.maxAppCount( ) )
                .planMaxMemberCount( currentPlan.maxMemberCount( ) )
                .planMaxStorage( currentPlan.maxStorage( ) )
                .isPackagesExpired( tenant.isPackagesExpired( ) )
                .packagesExpireAt( tenant.packagesExpiredAt( ) )
                .extraMemberCount( packages.getExtraMemberCount( ) )
                .extraStorage( packages.getExtraStorage( ) )
                .extraRemainSmsCount( packages.getExtraRemainSmsCount( ) )
                .usedAppCount( resourceUsage.getAppCount( ) )
                .effectiveMaxAppCount( max( packages.effectiveMaxAppCount( ), 0 ) )
                .usedMemberCount( resourceUsage.getMemberCount( ) )
                .effectiveMaxMemberCount( max( packages.effectiveMaxMemberCount( ), 0 ) )
                .usedSubmissionCount( resourceUsage.allSubmissionCount( ) )
                .effectiveMaxSubmissionCount( max( packages.effectiveMaxSubmissionCount( ), 0 ) )
                .usedQrCount( resourceUsage.allQrCount( ) )
                .effectiveMaxQrCount( max( packages.effectiveMaxQrCount( ), 0 ) )
                .usedStorage( valueOf( resourceUsage.getStorage( ) ).setScale( 2, HALF_UP ).toString( ) )
                .effectiveMaxStorage( valueOf( max( packages.effectiveMaxStorage( ), 0 ) ).setScale( 2, HALF_UP ).toString( ) )
                .usedSmsCountForCurrentMonth( resourceUsage.getSmsSentCountForCurrentMonth( ) )
                .effectiveMaxSmsCountPerMonth( max( packages.effectiveMaxSmsCountPerMonth( ), 0 ) )
                .effectiveMaxGroupCountPerApp( max( packages.effectiveMaxGroupCountPerApp( ), 0 ) )
                .effectiveMaxDepartmentCount( max( packages.effectiveMaxDepartmentCount( ), 0 ) )
                .build( );
    }

    public TenantBaseSetting fetchTenantBaseSetting( User user ) {
        user.checkIsTenantAdmin( );
        String tenantId = user.tenantId( );
        rateLimiter.applyFor( tenantId, "Tenant:FetchBaseSetting", 5 );

        Tenant tenant = tenantRepository.findPresent( tenantId );
        return TenantBaseSetting.builder( )
                .id( tenant.identifier( ) )
                .name( tenant.getName( ) )
                .loginBackground( tenant.getLoginBackground( ) )
                .build( );
    }

    public TenantLogo fetchTenantLogo( User user ) {
        user.checkIsTenantAdmin( );
        String tenantId = user.tenantId( );
        rateLimiter.applyFor( tenantId, "Tenant:FetchLogo", 5 );

        Tenant tenant = tenantRepository.findPresent( tenantId );
        return TenantLogo.builder( )
                .logo( tenant.getLogo( ) )
                .build( );
    }

    public TenantSubdomain fetchTenantSubdomain( User user ) {
        user.checkIsTenantAdmin( );
        String tenantId = user.tenantId( );
        rateLimiter.applyFor( tenantId, "Tenant:FetchSubdomain", 5 );

        Tenant tenant = tenantRepository.findPresent( tenantId );
        return TenantSubdomain.builder( )
                .subdomainPrefix( tenant.getSubdomainPrefix( ) )
                .updatable( tenant.subdomainUpdatable( ) )
                .build( );
    }

    public TenantApiSetting fetchTenantApiSetting( User user ) {
        user.checkIsTenantAdmin( );
        String tenantId = user.tenantId( );
        rateLimiter.applyFor( tenantId, "Tenant:FetchApiSetting", 5 );

        Tenant tenant = tenantRepository.findPresent( tenantId );
        return TenantApiSetting.builder( )
                .apiSetting( tenant.getApiSetting( ) )
                .build( );
    }

    public TenantPublicProfile fetchTenantPublicProfile( String subdomainPrefix ) {
        rateLimiter.applyFor( "Tenant:FetchPublicProfile:" + subdomainPrefix, 100 );

        Tenant tenant = tenantRepository.findBySubdomainPrefix( subdomainPrefix );
        return TenantPublicProfile.builder( )
                .tenantId( tenant.identifier( ) )
                .name( tenant.getName( ) )
                .logo( tenant.getLogo( ) )
                .loginBackground( tenant.getLoginBackground( ) )
                .build( );
    }

    public TenantInvoiceTitle fetchTenantInvoiceTitle( User user ) {
        user.checkIsTenantAdmin( );
        String tenantId = user.tenantId( );
        rateLimiter.applyFor( tenantId, "Tenant:FetchInvoiceTitle", 5 );

        Tenant tenant = tenantRepository.findPresent( tenantId );
        return TenantInvoiceTitle.builder( )
                .title( tenant.getInvoiceTitle( ) )
                .build( );
    }

    public List< Consignee > listConsignees( User user ) {
        user.checkIsTenantAdmin( );
        String tenantId = user.tenantId( );
        rateLimiter.applyFor( tenantId, "Tenant:ListConsignees", 5 );

        Tenant tenant = tenantRepository.findPresent( tenantId );
        return tenant.getConsignees( );
    }
}
