package io.github.yeahfo.fit.core.tenant.application;

import io.github.yeahfo.fit.core.common.domain.UploadedFile;
import lombok.Builder;

@Builder
public record TenantBaseSetting( String id,
                                 String name,
                                 UploadedFile loginBackground ) {
}
