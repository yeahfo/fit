package io.github.yeahfo.fit.core.department.infrastructure;

import io.github.yeahfo.fit.core.department.domain.Department;
import io.github.yeahfo.fit.core.department.domain.DepartmentRepository;
import io.github.yeahfo.fit.core.department.domain.TenantCachedDepartment;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static io.github.yeahfo.fit.core.common.utils.CommonUtils.requireNonBlank;
import static io.github.yeahfo.fit.core.common.utils.FitConstants.TENANT_DEPARTMENTS_CACHE;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
@RequiredArgsConstructor
public class DepartmentRepositoryImpl implements DepartmentRepository {
    private final MongoTemplate mongoTemplate;
    private final DepartmentRepositoryImplementation implementation;

    @Override
    public Department save( Department department ) {
        return implementation.save( department );
    }

    @Override
    public Optional< Department > findById( String id ) {
        return implementation.findById( id );
    }

    @Override
    public void deleteById( String id ) {
        implementation.deleteById( id );
    }

    @Override
    @Cacheable( value = TENANT_DEPARTMENTS_CACHE, key = "#tenantId" )
    public List< TenantCachedDepartment > tenantCachedDepartments( String tenantId ) {
        requireNonBlank( tenantId, "Tenant ID must not be blank." );

        Query query = query( where( "tenantId" ).is( tenantId ) );
        query.fields( ).include( "name", "managers", "customId" );
        return mongoTemplate.find( query, TenantCachedDepartment.class );
    }
}
