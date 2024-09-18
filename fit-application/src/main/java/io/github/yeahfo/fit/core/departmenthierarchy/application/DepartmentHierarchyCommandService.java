package io.github.yeahfo.fit.core.departmenthierarchy.application;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.github.yeahfo.fit.core.common.domain.user.User;
import io.github.yeahfo.fit.core.departmenthierarchy.domain.DepartmentHierarchy;
import io.github.yeahfo.fit.core.departmenthierarchy.domain.DepartmentHierarchyRepository;
import io.github.yeahfo.fit.core.departmenthierarchy.domain.events.DepartmentHierarchyDomainEvent;
import io.github.yeahfo.fit.core.departmenthierarchy.domain.events.DepartmentHierarchyDomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DepartmentHierarchyCommandService {

    private final DepartmentHierarchyRepository departmentHierarchyRepository;
    private final DepartmentHierarchyDomainEventPublisher domainEventPublisher;

    @Transactional
    public void create( User user ) {
        ResultWithDomainEvents< DepartmentHierarchy, DepartmentHierarchyDomainEvent > resultWithDomainEvents = DepartmentHierarchy.create( user );
        DepartmentHierarchy departmentHierarchy = departmentHierarchyRepository.save( resultWithDomainEvents.result );
        domainEventPublisher.publish( departmentHierarchy, resultWithDomainEvents.events );
    }
}
