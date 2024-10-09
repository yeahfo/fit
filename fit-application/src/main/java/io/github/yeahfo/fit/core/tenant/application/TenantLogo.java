package io.github.yeahfo.fit.core.tenant.application;

import io.github.yeahfo.fit.core.common.domain.UploadedFile;
import lombok.Builder;

@Builder
public record TenantLogo( UploadedFile logo ) {
}
