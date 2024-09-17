package io.github.yeahfo.fit.core.customer.domain;

import java.util.Optional;

public interface CustomerRepository {
    Customer save( Customer customer );

    Optional< Customer> findById( Long id );
}
