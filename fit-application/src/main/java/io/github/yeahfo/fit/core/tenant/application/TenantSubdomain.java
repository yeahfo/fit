package io.github.yeahfo.fit.core.tenant.application;

import lombok.Builder;

@Builder
public record TenantSubdomain( String subdomainPrefix,
                               boolean updatable ) {
}
