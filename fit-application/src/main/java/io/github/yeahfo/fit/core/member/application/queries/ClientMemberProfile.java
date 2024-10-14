package io.github.yeahfo.fit.core.member.application.queries;

import io.github.yeahfo.fit.core.common.domain.UploadedFile;
import lombok.Builder;

import java.util.List;

@Builder
public record ClientMemberProfile( String memberId,
                                   String memberName,
                                   UploadedFile avatar,
                                   String tenantId,
                                   String tenantName,
                                   UploadedFile tenantLogo,
                                   String subdomainPrefix,
                                   boolean subdomainReady,
                                   List< String > topAppIds,
                                   boolean hideBottomMryLogo,
                                   boolean reportingAllowed,
                                   boolean kanbanAllowed,
                                   boolean assignmentAllowed ) {
}
