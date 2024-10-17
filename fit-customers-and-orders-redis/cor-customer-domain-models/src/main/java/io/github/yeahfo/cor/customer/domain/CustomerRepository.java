package io.github.yeahfo.cor.customer.domain;

import java.util.Optional;

public interface CustomerRepository {
    Customer save( Customer customer );

    Optional< Customer > findById( Long id );
}
