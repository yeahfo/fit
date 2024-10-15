package io.github.yeahfo.fit.core.app.domain;

import io.github.yeahfo.fit.core.group.domain.Group;
import io.github.yeahfo.fit.core.grouphierarchy.domain.GroupHierarchy;
import lombok.Builder;

@Builder
public record CreateAppResult( App app,
                               Group defaultGroup,
                               GroupHierarchy groupHierarchy ) {

}
