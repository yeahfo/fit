package io.github.yeahfo.cor.customer.configuration;

import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.github.yeahfo.cor.customer.application.CustomerService;
import io.github.yeahfo.cor.customer.domain.CustomerRepository;
import io.github.yeahfo.cor.customer.domain.events.CustomerDomainEventPublisher;
import io.github.yeahfo.cro.customer.infrastructure.CustomerRepositoryImpl;
import io.github.yeahfo.cro.customer.infrastructure.CustomerRepositoryImplementation;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableAutoConfiguration( exclude = {
        RedisAutoConfiguration.class
} )
@EnableJpaRepositories( basePackageClasses = {
        CustomerRepositoryImplementation.class
} )
public class CustomerConfiguration {

    @Bean
    CustomerService customerService( CustomerRepository customerRepository,
                                     CustomerDomainEventPublisher domainEventPublisher ) {
        return new CustomerService( customerRepository, domainEventPublisher );
    }

    @Bean
    CustomerRepository customerRepository( CustomerRepositoryImplementation implementation ) {
        return new CustomerRepositoryImpl( implementation );
    }

    @Bean
    CustomerDomainEventPublisher customerDomainEventPublisher( DomainEventPublisher domainEventPublisher ) {
        return new CustomerDomainEventPublisher( domainEventPublisher );
    }
}
