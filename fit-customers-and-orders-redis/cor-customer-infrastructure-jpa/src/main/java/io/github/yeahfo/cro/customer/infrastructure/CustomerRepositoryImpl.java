package io.github.yeahfo.cro.customer.infrastructure;

import io.github.yeahfo.cor.customer.domain.Customer;
import io.github.yeahfo.cor.customer.domain.CustomerRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {
    private final CustomerRepositoryImplementation implementation;

    @Override
    public Customer save( Customer customer ) {
        return implementation.save( customer );
    }

    @Override
    public Optional< Customer > findById( Long id ) {
        return implementation.findById( id );
    }
}
