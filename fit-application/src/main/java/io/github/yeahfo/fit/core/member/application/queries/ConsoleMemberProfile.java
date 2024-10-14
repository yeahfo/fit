package io.github.yeahfo.fit.core.member.application.queries;

import io.github.yeahfo.fit.core.common.domain.UploadedFile;
import io.github.yeahfo.fit.core.common.domain.user.Role;
import io.github.yeahfo.fit.core.tenant.application.ConsoleTenantProfile;
import lombok.Builder;

import java.util.List;

@Builder
public record ConsoleMemberProfile( String memberId,
                                    String tenantId,
                                    String name,
                                    Role role,
                                    UploadedFile avatar,
                                    boolean hasManagedApps,
                                    ConsoleTenantProfile tenantProfile,
                                    List< String > topAppIds,
                                    boolean mobileIdentified ) {
}
