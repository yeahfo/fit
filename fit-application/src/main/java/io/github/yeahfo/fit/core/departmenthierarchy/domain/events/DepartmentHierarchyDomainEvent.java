package io.github.yeahfo.fit.core.departmenthierarchy.domain.events;

import io.github.yeahfo.fit.core.common.domain.events.DomainEvent;

public interface DepartmentHierarchyDomainEvent extends DomainEvent {
    String aggregateType
            = "io.github.yeahfo.fit.core.departmenthierarchy.domain.DepartmentHierarchy";
}
