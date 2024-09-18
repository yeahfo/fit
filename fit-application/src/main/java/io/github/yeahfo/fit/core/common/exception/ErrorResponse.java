package io.github.yeahfo.fit.core.common.exception;

import lombok.Builder;

@Builder
public record ErrorResponse(Error error ) {
}
