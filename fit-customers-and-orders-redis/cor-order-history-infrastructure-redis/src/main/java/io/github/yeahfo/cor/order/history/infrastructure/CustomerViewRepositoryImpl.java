package io.github.yeahfo.cor.order.history.infrastructure;

import io.github.yeahfo.cor.order.history.domain.CustomerView;
import io.github.yeahfo.cor.order.history.domain.CustomerViewRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class CustomerViewRepositoryImpl implements CustomerViewRepository {
    private final CustomerViewRepositoryImplementation implementation;

    @Override
    public CustomerView save( CustomerView customerView ) {
        return implementation.save( customerView );
    }

    @Override
    public Optional< CustomerView > findById( long id ) {
        return implementation.findById( id );
    }
}
