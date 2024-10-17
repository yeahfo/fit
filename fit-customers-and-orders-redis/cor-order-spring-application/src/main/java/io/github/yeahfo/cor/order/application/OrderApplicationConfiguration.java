package io.github.yeahfo.cor.order.application;

import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.github.yeahfo.cor.order.application.consumers.OrderApplicationEventSubscriber;
import io.github.yeahfo.cor.order.domain.OrderRepository;
import io.github.yeahfo.cor.order.domain.events.OrderDomainEventPublisher;
import io.github.yeahfo.cor.order.infrastructure.OrderRepositoryImpl;
import io.github.yeahfo.cor.order.infrastructure.OrderRepositoryImplementation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories( basePackageClasses = {
        OrderRepositoryImplementation.class
} )
public class OrderApplicationConfiguration {
    @Bean
    OrderRepository orderRepository( OrderRepositoryImplementation implementation ) {
        return new OrderRepositoryImpl( implementation );
    }

    @Bean
    OrderService orderService( OrderRepository orderRepository,
                               OrderDomainEventPublisher domainEventPublisher ) {
        return new OrderService( orderRepository, domainEventPublisher );
    }

    @Bean
    OrderDomainEventPublisher orderDomainEventPublisher( DomainEventPublisher domainEventPublisher ) {
        return new OrderDomainEventPublisher( domainEventPublisher );
    }

    @Bean
    DomainEventDispatcher orderDomainEventDispatcher( DomainEventDispatcherFactory factory,
                                                      OrderApplicationEventSubscriber subscriber ) {
        return factory.make( subscriber.getClass( ).getSimpleName( ), subscriber.domainEventHandlers( ) );
    }
}
