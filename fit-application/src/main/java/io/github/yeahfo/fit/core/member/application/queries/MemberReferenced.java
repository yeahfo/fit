package io.github.yeahfo.fit.core.member.application.queries;

import lombok.Builder;

@Builder
public record MemberReferenced( String id,
                                String showName ) {
}
