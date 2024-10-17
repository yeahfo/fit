package io.github.yeahfo.cor.order.application;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.github.yeahfo.cor.order.application.commands.CreateOrderCommand;
import io.github.yeahfo.cor.order.application.commands.CreateOrderResult;
import io.github.yeahfo.cor.order.application.commands.GetOrderResult;
import io.github.yeahfo.cor.order.domain.Order;
import io.github.yeahfo.cor.order.domain.OrderDetails;
import io.github.yeahfo.cor.order.domain.OrderRepository;
import io.github.yeahfo.cor.order.domain.events.OrderDomainEvent;
import io.github.yeahfo.cor.order.domain.events.OrderDomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDomainEventPublisher domainEventPublisher;

    @Transactional
    public CreateOrderResult create( CreateOrderCommand cmd ) {
        ResultWithDomainEvents< Order, OrderDomainEvent > resultWithDomainEvents = Order.create( new OrderDetails( cmd.customerId( ), cmd.orderTotal( ) ) );
        Order order = this.orderRepository.save( resultWithDomainEvents.result );
        this.domainEventPublisher.publish( order, resultWithDomainEvents.events );
        return CreateOrderResult.builder( ).orderId( order.identifier( ) ).build( );
    }

    @Transactional
    public void approveOrder( Long orderId ) {
        ResultWithDomainEvents< Order, OrderDomainEvent > resultWithDomainEvents = orderRepository.findBy( orderId )
                .approve( );
        Order order = this.orderRepository.save( resultWithDomainEvents.result );
        this.domainEventPublisher.publish( order, resultWithDomainEvents.events );
    }

    @Transactional
    public void rejectOrder( Long orderId ) {
        ResultWithDomainEvents< Order, OrderDomainEvent > resultWithDomainEvents = orderRepository.findBy( orderId )
                .reject( );
        Order order = this.orderRepository.save( resultWithDomainEvents.result );
        this.domainEventPublisher.publish( order, resultWithDomainEvents.events );
    }

    public GetOrderResult getOrder( Long orderId ) {
        Order order = orderRepository.findBy( orderId );
        return GetOrderResult.builder( )
                .orderId( order.identifier( ) )
                .orderState( order.state( ) )
                .orderDetails( order.orderDetails( ) )
                .build( );
    }
}
