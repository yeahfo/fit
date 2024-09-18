package io.github.yeahfo.fit.core.member.domain;

import io.github.yeahfo.fit.core.common.domain.AggregateRootRepository;

public interface MemberRepository extends AggregateRootRepository< Member, String > {
    long countByTenantId( String tenantId );
}
