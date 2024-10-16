package io.github.yeahfo.cor.customer.web;

import io.github.yeahfo.cor.customer.application.CustomerService;
import io.github.yeahfo.cor.customer.application.commands.CreateCustomerCommand;
import io.github.yeahfo.cor.customer.application.commands.CreateCustomerResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping( "/customers" )
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public CreateCustomerResult create( @RequestBody CreateCustomerCommand command ) {
        return customerService.createCustomer( command );
    }
}
