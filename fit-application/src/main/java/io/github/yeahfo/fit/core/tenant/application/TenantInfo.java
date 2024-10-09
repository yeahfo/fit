package io.github.yeahfo.fit.core.tenant.application;

import io.github.yeahfo.fit.core.plan.domain.PlanType;
import lombok.Builder;

import java.time.Instant;

@Builder
public record TenantInfo( String tenantId,
                          String name,
                          PlanType planType,
                          Instant createdAt,
                          String createdBy,
                          String creator,
                          String packagesName,

                          long planMaxAppCount,
                          long planMaxMemberCount,
                          float planMaxStorage,

                          boolean isPackagesExpired,
                          Instant packagesExpireAt,

                          int extraMemberCount,
                          int extraStorage,
                          int extraRemainSmsCount,

                          long usedAppCount,
                          long effectiveMaxAppCount,

                          long usedMemberCount,
                          long effectiveMaxMemberCount,

                          String usedStorage,
                          String effectiveMaxStorage,

                          int usedSubmissionCount,
                          long effectiveMaxSubmissionCount,

                          int usedQrCount,
                          long effectiveMaxQrCount,

                          int usedSmsCountForCurrentMonth,
                          long effectiveMaxSmsCountPerMonth,

                          long effectiveMaxGroupCountPerApp,
                          long effectiveMaxDepartmentCount ) {
}
