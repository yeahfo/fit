package io.github.yeahfo.fit.core.tenant.application;

import io.github.yeahfo.fit.core.common.domain.UploadedFile;
import lombok.Builder;

@Builder
public record ConsoleTenantProfile( String tenantId,
                                    String name,
                                    UploadedFile logo,
                                    String subdomainPrefix,
                                    String baseDomainName,
                                    boolean subdomainReady,
                                    PackagesStatusInfo packagesStatus ) {
}
