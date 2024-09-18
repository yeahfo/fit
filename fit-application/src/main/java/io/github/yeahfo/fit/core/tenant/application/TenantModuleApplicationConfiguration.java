package io.github.yeahfo.fit.core.tenant.application;

import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TenantModuleApplicationConfiguration {

    @Bean
    DomainEventDispatcher tenantModuleDomainEventDispatcher( DomainEventDispatcherFactory factory,
                                                             TenantModuleEventsSubscriber subscriber ) {
        return factory.make( subscriber.getClass( ).getSimpleName( ), subscriber.domainEventHandlers( ) );
    }
}
