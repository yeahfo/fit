package io.github.yeahfo.fit.core.tenant.domain;

import io.github.yeahfo.fit.core.common.domain.AggregateRootRepository;

public interface TenantRepository extends AggregateRootRepository< Tenant, String > {
    boolean existsById( String id );
}
