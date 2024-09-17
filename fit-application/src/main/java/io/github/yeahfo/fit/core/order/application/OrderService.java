package io.github.yeahfo.fit.core.order.application;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.github.yeahfo.fit.core.order.domain.Order;
import io.github.yeahfo.fit.core.order.domain.OrderRepository;
import io.github.yeahfo.fit.core.order.domain.events.OrderDomainEvent;
import io.github.yeahfo.fit.core.order.domain.events.OrderDomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repository;
    private final OrderDomainEventPublisher domainEventPublisher;

    @Transactional
    public Long create( CreateOrderCommand command ) {
        ResultWithDomainEvents< Order, OrderDomainEvent > resultWithDomainEvents = Order.create( command.details( ) );
        Order order = repository.save( resultWithDomainEvents.result );
        domainEventPublisher.publish( order, resultWithDomainEvents.events );
        return order.getId( );
    }

    @Transactional
    public void approveOrder( long id ) {
        Order order = repository.findOrder( id );
        ResultWithDomainEvents< Order, OrderDomainEvent > resultWithDomainEvents = order.noteCreditReserved( );
        repository.save( resultWithDomainEvents.result );
        domainEventPublisher.publish( order, resultWithDomainEvents.events );
    }

    @Transactional
    public void rejectOrder( long id ) {
        Order order = repository.findOrder( id );
        ResultWithDomainEvents< Order, OrderDomainEvent > resultWithDomainEvents = order.noteCreditReservationFailed( );
        repository.save( order );
        domainEventPublisher.publish( order, resultWithDomainEvents.events );
    }
}
