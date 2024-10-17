package io.github.yeahfo.cor.order.history.web;

import io.github.yeahfo.cor.order.history.application.OrderHistoryViewService;
import io.github.yeahfo.cor.order.history.domain.CustomerView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderHistoryController {
    private final OrderHistoryViewService orderHistoryViewService;

    @GetMapping( value = "/customers/{customerId}" )
    public ResponseEntity< CustomerView > getCustomer( @PathVariable Long customerId ) {
        return orderHistoryViewService.getCustomer( customerId )
                .map( ResponseEntity::ok )
                .orElseGet( ( ) -> ResponseEntity.notFound( ).build( ) );
    }
}
