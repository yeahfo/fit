package io.github.yeahfo.cor.order.infrastructure;

import io.github.yeahfo.cor.order.domain.Order;
import io.github.yeahfo.cor.order.domain.OrderRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {
    private final OrderRepositoryImplementation implementation;

    @Override
    public Order save( Order order ) {
        return implementation.save( order );
    }

    @Override
    public Optional< Order > findById( Long id ) {
        return implementation.findById( id );
    }
}
