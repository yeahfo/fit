package io.github.yeahfo.fit.core.order.application;

import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.github.yeahfo.fit.core.order.application.consumer.OrderModuleEventsSubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderApplicationConfiguration {
    @Bean
    DomainEventDispatcher orderModulesEventDispatcher( DomainEventDispatcherFactory factory,
                                                       OrderModuleEventsSubscriber subscriber ) {
        return factory.make( subscriber.getClass( ).getSimpleName( ), subscriber.domainEventHandlers( ) );
    }
}
