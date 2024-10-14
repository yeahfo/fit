package io.github.yeahfo.fit.core.app.domain;

import io.github.yeahfo.fit.core.common.domain.AggregateRootRepository;

public interface AppRepository extends AggregateRootRepository< App, String > {
    boolean existsByTenantIdAndManagers( String tenantId, String managers );
}
