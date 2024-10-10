package io.github.yeahfo.fit.core.common.application;

import lombok.Builder;

import java.util.Collection;

@Builder
public record PagedResponse< T >( Integer page,
                                  Integer size,
                                  Long total,
                                  Collection< T > content ) {

}
