package io.github.yeahfo.fit.core.grouphierarchy.domain;

import io.github.yeahfo.fit.core.common.domain.AggregateRoot;
import io.github.yeahfo.fit.core.common.domain.idnode.IdTree;
import io.github.yeahfo.fit.core.common.domain.idnode.IdTreeHierarchy;

public class GroupHierarchy extends AggregateRoot {
    private String appId;
    private IdTree idTree;
    private IdTreeHierarchy hierarchy;
}
