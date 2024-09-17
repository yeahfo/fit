package io.github.yeahfo.fit.core.customer.infrastructure;

import io.github.yeahfo.fit.core.customer.domain.Customer;
import io.github.yeahfo.fit.core.customer.domain.CustomerRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepositoryImplementation extends CustomerRepository, MongoRepository< Customer, Long > {
}
