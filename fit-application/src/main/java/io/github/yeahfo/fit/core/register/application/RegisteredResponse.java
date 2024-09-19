package io.github.yeahfo.fit.core.register.application;

import lombok.Builder;

@Builder
public record RegisteredResponse( String tenantId,
                                  String memberId ) {
}
