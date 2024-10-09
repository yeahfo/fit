package io.github.yeahfo.fit.core.tenant.infrastructure;

import io.github.yeahfo.fit.core.tenant.domain.Tenant;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TenantRepositoryImplementation extends MongoRepository< Tenant, String > {
    boolean existsBySubdomainPrefix( String subdomainPrefix );

    Optional< Tenant > findBySubdomainPrefix( String subdomainPrefix );
}
