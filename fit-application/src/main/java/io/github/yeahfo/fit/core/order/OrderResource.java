package io.github.yeahfo.fit.core.order;

import io.github.yeahfo.fit.core.order.application.CreateOrderCommand;
import io.github.yeahfo.fit.core.order.application.OrderService;
import io.github.yeahfo.fit.core.order.domain.Order;
import io.github.yeahfo.fit.core.order.infrastructure.OrderRepositoryImplementation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping( "/orders" )
@Tag( name = "CustomersAndOrders" )
public class OrderResource {
    private final OrderService orderService;
    private final OrderRepositoryImplementation orderRepository;

    @PostMapping
    @ResponseStatus( CREATED )
    public Long createOrder( @RequestBody CreateOrderCommand command ) {
        return orderService.create( command );
    }

    @GetMapping
    public List< Order > listOrders( ) {
        return orderRepository.findAll( Sort.by( "id" ).descending( ) );
    }
}
