package io.github.yeahfo.fit.core.tenant.infrastructure;

import io.github.yeahfo.fit.core.tenant.domain.Tenant;
import io.github.yeahfo.fit.core.tenant.domain.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class TenantRepositoryImpl implements TenantRepository {
    private final TenantRepositoryImplementation implementation;

    @Override
    public Tenant save( Tenant tenant ) {
        return implementation.save( tenant );
    }

    @Override
    public Optional< Tenant > findById( String id ) {
        return implementation.findById( id );
    }

    @Override
    public void deleteById( String id ) {
        implementation.deleteById( id );
    }

    @Override
    public boolean existsById( String id ) {
        return implementation.existsById( id );
    }
}
