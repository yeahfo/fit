package io.github.yeahfo.fit.core.department.domain;

import io.github.yeahfo.fit.core.common.domain.AggregateRootRepository;

import java.util.List;

public interface DepartmentRepository extends AggregateRootRepository< Department, String > {
    List< TenantCachedDepartment > tenantCachedDepartments( String tenantId );
}
