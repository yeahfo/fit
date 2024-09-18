package io.github.yeahfo.fit.core.tenant.domain.events;


import io.eventuate.tram.events.aggregates.AbstractAggregateDomainEventPublisher;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.github.yeahfo.fit.core.tenant.domain.Tenant;
import org.springframework.stereotype.Component;

@Component
public class TenantDomainEventPublisher extends AbstractAggregateDomainEventPublisher< Tenant, TenantDomainEvent > {
    public TenantDomainEventPublisher( DomainEventPublisher eventPublisher ) {
        super( eventPublisher, Tenant.class, Tenant::identifier );
    }
}
