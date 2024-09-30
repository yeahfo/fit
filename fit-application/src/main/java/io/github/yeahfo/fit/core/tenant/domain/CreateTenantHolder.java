package io.github.yeahfo.fit.core.tenant.domain;

import io.github.yeahfo.fit.core.departmenthierarchy.domain.DepartmentHierarchy;
import io.github.yeahfo.fit.core.member.domain.Member;
import lombok.Builder;

@Builder
public record CreateTenantHolder( Tenant tenant,
                                  Member member,
                                  DepartmentHierarchy departmentHierarchy ) {
}
