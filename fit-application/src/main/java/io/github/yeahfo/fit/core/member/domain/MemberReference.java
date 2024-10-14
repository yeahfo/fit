package io.github.yeahfo.fit.core.member.domain;

import lombok.Builder;

@Builder
public record MemberReference( String id,
                               String name,
                               String mobile,
                               String email ) {
}
