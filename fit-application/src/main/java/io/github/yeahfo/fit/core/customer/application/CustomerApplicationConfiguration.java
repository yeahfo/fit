package io.github.yeahfo.fit.core.customer.application;

import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.github.yeahfo.fit.core.customer.application.consumer.CustomerModuleEventsSubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomerApplicationConfiguration {

    @Bean
    DomainEventDispatcher customerModuleEventDispatcher( DomainEventDispatcherFactory factory,
                                                         CustomerModuleEventsSubscriber subscriber ) {
        return factory.make( subscriber.getClass( ).getSimpleName( ), subscriber.domainEventHandlers( ) );
    }
}
