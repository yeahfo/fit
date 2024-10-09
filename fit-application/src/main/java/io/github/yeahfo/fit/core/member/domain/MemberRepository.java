package io.github.yeahfo.fit.core.member.domain;

import io.github.yeahfo.fit.core.common.domain.AggregateRootRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends AggregateRootRepository< Member, String > {
    long countByTenantId( String tenantId );

    Optional< Member > findByMobileOrEmail( String mobileOrEmail );

    boolean existsByMobileOrEmail( String mobileOrEmail );

    boolean existsByMobile( String mobile );

    boolean existsByEmail( String email );

    List< TenantCachedMember > tenantCachedMembers( String tenantId );

}
