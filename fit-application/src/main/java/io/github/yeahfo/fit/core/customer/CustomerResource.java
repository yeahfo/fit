package io.github.yeahfo.fit.core.customer;

import io.github.yeahfo.fit.core.customer.application.CreateCustomerCommand;
import io.github.yeahfo.fit.core.customer.application.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping( "/customers" )
public class CustomerResource {
    private final CustomerService customerService;

    @PostMapping
    public Long customers( @RequestBody CreateCustomerCommand command ) {
        return customerService.create( command );
    }
}
