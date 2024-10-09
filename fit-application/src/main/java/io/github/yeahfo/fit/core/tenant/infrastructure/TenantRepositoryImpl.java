package io.github.yeahfo.fit.core.tenant.infrastructure;

import io.github.yeahfo.fit.core.common.exception.FitException;
import io.github.yeahfo.fit.core.tenant.domain.PackagesStatus;
import io.github.yeahfo.fit.core.tenant.domain.Tenant;
import io.github.yeahfo.fit.core.tenant.domain.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static io.github.yeahfo.fit.core.common.exception.ErrorCode.TENANT_NOT_FOUND;
import static io.github.yeahfo.fit.core.common.utils.CommonUtils.requireNonBlank;
import static io.github.yeahfo.fit.core.common.utils.FitConstants.TENANT_COLLECTION;
import static io.github.yeahfo.fit.core.common.utils.MapUtils.mapOf;
import static org.springframework.data.mongodb.core.query.Criteria.where;


@Repository
@RequiredArgsConstructor
public class TenantRepositoryImpl implements TenantRepository {
    private final MongoTemplate mongoTemplate;
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

    @Override
    public PackagesStatus packagesStatusOf( String tenantId ) {
        requireNonBlank( tenantId, "Tenant ID must not be blank." );

        Query query = Query.query( where( "_id" ).is( tenantId ) );
        query.fields( ).include( "packages" ).include( "resourceUsage" );
        PackagesStatus packagesStatus = mongoTemplate.findOne( query, PackagesStatus.class, TENANT_COLLECTION );

        if ( packagesStatus == null ) {
            throw new FitException( TENANT_NOT_FOUND, "没有找到租户。", mapOf( "id", tenantId ) );
        }

        return packagesStatus;
    }

    @Override
    public boolean existsBySubdomainPrefix( String subdomainPrefix ) {
        return implementation.existsBySubdomainPrefix( subdomainPrefix );
    }

    @Override
    public Tenant findBySubdomainPrefix( String subdomainPrefix ) {
        return implementation.findBySubdomainPrefix( subdomainPrefix ).orElseThrow( ( ) -> new FitException( TENANT_NOT_FOUND, "找不到域名对应租户" ) );
    }
}
