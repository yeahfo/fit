package io.github.yeahfo.fit.core.tenant.application;

import io.github.yeahfo.fit.core.tenant.domain.ApiSetting;
import lombok.Builder;

@Builder
public record TenantApiSetting( ApiSetting apiSetting ) {
}
