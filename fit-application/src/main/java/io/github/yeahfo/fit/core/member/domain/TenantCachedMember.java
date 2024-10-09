package io.github.yeahfo.fit.core.member.domain;

import io.github.yeahfo.fit.core.common.domain.user.Role;
import lombok.Builder;

import java.util.List;

import static io.github.yeahfo.fit.core.common.domain.user.Role.TENANT_ADMIN;

@Builder
public record TenantCachedMember( String id,
                                  String name,
                                  Role role,
                                  String mobile,
                                  String email,
                                  String mobileWxOpenId,
                                  String customId,
                                  List< String > departmentIds,
                                  boolean active ) {
    public boolean isTenantAdmin( ) {
        return this.role == TENANT_ADMIN;
    }
}
