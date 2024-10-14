package io.github.yeahfo.fit.core.member.application.queries;

import io.github.yeahfo.fit.core.common.domain.user.Role;
import lombok.Builder;

import java.util.List;

@Builder
public record MemberInfo( String memberId,
                          String tenantId,
                          String name,
                          String email,
                          String mobile,
                          Role role,
                          String wxNickName,
                          boolean wxBound,
                          List< String > departments ) {
}
