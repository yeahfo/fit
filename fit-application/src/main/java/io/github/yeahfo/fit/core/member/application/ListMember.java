package io.github.yeahfo.fit.core.member.application;

import io.github.yeahfo.fit.core.common.domain.UploadedFile;
import io.github.yeahfo.fit.core.common.domain.user.Role;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record ListMember( String id,
                          String name,
                          List< String > departmentIds,
                          UploadedFile avatar,
                          boolean active,
                          Role role,
                          String mobile,
                          String wxUnionId,
                          String wxNickName,
                          String email,
                          Instant createdAt ) {
}
