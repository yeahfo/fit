package io.github.yeahfo.cor.order.history.application;

import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.github.yeahfo.cor.order.history.application.consumers.OrderHistoryApplicationEventSubscriber;
import io.github.yeahfo.cor.order.history.domain.CustomerViewRepository;
import io.github.yeahfo.cor.order.history.infrastructure.CustomerViewRepositoryImpl;
import io.github.yeahfo.cor.order.history.infrastructure.CustomerViewRepositoryImplementation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableRedisRepositories( basePackageClasses = {
        CustomerViewRepositoryImplementation.class,
} )
public class OrderHistoryApplicationConfiguration {

    @Bean
    DomainEventDispatcher orderHistoryApplicationDomainEventDispatcher( DomainEventDispatcherFactory factory,
                                                                        OrderHistoryApplicationEventSubscriber subscriber ) {
        return factory.make( subscriber.getClass( ).getSimpleName( ), subscriber.domainEventHandlers( ) );
    }

    @Bean
    OrderHistoryViewService orderHistoryViewService( CustomerViewRepository customerViewRepository ) {
        return new OrderHistoryViewService( customerViewRepository );
    }

    @Bean
    CustomerViewRepository customerViewRepository( CustomerViewRepositoryImplementation implementation ) {
        return new CustomerViewRepositoryImpl( implementation );
    }

    @Bean
    public StringRedisTemplate redisTemplate( RedisConnectionFactory connectionFactory ) {
        StringRedisTemplate template = new StringRedisTemplate( connectionFactory );
        // explicitly enable transaction support
        template.setEnableTransactionSupport( true );
        return template;
    }
}
