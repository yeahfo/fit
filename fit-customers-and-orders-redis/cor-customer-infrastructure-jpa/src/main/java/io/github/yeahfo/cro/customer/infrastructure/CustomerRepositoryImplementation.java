package io.github.yeahfo.cro.customer.infrastructure;

import io.github.yeahfo.cor.customer.domain.Customer;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

public interface CustomerRepositoryImplementation extends JpaRepositoryImplementation< Customer, Long > {
}
