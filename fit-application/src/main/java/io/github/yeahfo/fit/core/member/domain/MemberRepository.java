package io.github.yeahfo.fit.core.member.domain;

import io.github.yeahfo.fit.core.common.domain.AggregateRootRepository;
import io.github.yeahfo.fit.core.member.application.ListMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends AggregateRootRepository< Member, String > {
    long countByTenantId( String tenantId );

    Optional< Member > findByMobileOrEmail( String mobileOrEmail );

    boolean existsByMobileOrEmail( String mobileOrEmail );

    boolean existsByMobile( String mobile );

    boolean existsByEmail( String email );

    List< TenantCachedMember > tenantCachedMembers( String tenantId );

    Page< ListMember > listTenantMembers( int page, int size, Sort sort, String tenantId, String departmentId, String search );
}
