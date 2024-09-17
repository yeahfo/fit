package io.github.yeahfo.fit.core.order;

import io.github.yeahfo.fit.core.order.application.CreateOrderCommand;
import io.github.yeahfo.fit.core.order.application.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping( "/orders" )
public class OrderResource {
    private final OrderService orderService;


    @PostMapping
    @ResponseStatus( CREATED )
    public Long createOrder( @RequestBody CreateOrderCommand command ) {
        return orderService.create( command );
    }
}
