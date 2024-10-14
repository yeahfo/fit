package io.github.yeahfo.fit.core.departmenthierarchy.infrastructure;

import io.github.yeahfo.fit.core.departmenthierarchy.domain.DepartmentHierarchy;
import io.github.yeahfo.fit.core.departmenthierarchy.domain.DepartmentHierarchyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static io.github.yeahfo.fit.core.common.utils.FitConstants.DEPARTMENT_HIERARCHY_CACHE;

@Repository
@RequiredArgsConstructor
public class DepartmentHierarchyRepositoryImpl implements DepartmentHierarchyRepository {
    private final DepartmentHierarchyRepositoryImplementation implementation;

    @Override
    @CacheEvict( value = DEPARTMENT_HIERARCHY_CACHE, key = "#departmentHierarchy.tenantId()" )
    public DepartmentHierarchy save( DepartmentHierarchy departmentHierarchy ) {
        return implementation.save( departmentHierarchy );
    }

    @Override
    public Optional< DepartmentHierarchy > findById( String id ) {
        return implementation.findById( id );
    }

    @Override
    public void deleteById( String id ) {
        implementation.deleteById( id );
    }

    @Override
    @Cacheable( value = DEPARTMENT_HIERARCHY_CACHE, key = "#tenantId" )
    public DepartmentHierarchy findByTenantId( String tenantId ) {
        return implementation.findByTenantId( tenantId );
    }
}
