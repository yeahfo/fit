package io.github.yeahfo.fit.core.common.domain;

import lombok.Builder;

import java.time.Instant;

@Builder
public record OpsLog( Instant operatedAt,
                      String operatedBy,
                      String operator,
                      String note ) {
}
