package io.github.yeahfo.fit.core.tenant.infrastructure;

import io.github.yeahfo.fit.core.tenant.domain.Tenant;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TenantRepositoryImplementation extends MongoRepository< Tenant, String > {
    boolean existsBySubdomainPrefix( String subdomainPrefix );
}
