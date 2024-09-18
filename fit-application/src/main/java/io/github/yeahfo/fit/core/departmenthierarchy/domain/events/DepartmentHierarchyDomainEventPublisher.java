package io.github.yeahfo.fit.core.departmenthierarchy.domain.events;

import io.eventuate.tram.events.aggregates.AbstractAggregateDomainEventPublisher;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.github.yeahfo.fit.core.departmenthierarchy.domain.DepartmentHierarchy;
import org.springframework.stereotype.Component;

@Component
public class DepartmentHierarchyDomainEventPublisher extends AbstractAggregateDomainEventPublisher< DepartmentHierarchy, DepartmentHierarchyDomainEvent > {
    protected DepartmentHierarchyDomainEventPublisher( DomainEventPublisher eventPublisher ) {
        super( eventPublisher, DepartmentHierarchy.class, DepartmentHierarchy::identifier );
    }
}
