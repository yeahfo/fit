package io.github.yeahfo.fit.core.customer;

import io.github.yeahfo.fit.core.customer.application.CreateCustomerCommand;
import io.github.yeahfo.fit.core.customer.application.CustomerService;
import io.github.yeahfo.fit.core.customer.domain.Customer;
import io.github.yeahfo.fit.core.customer.infrastructure.CustomerRepositoryImplementation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping( "/customers" )
@Tag( name = "CustomersAndOrders" )
public class CustomerResource {
    private final CustomerService customerService;
    private final CustomerRepositoryImplementation customerRepository;

    @PostMapping
    public Long customers( @RequestBody CreateCustomerCommand command ) {
        return customerService.create( command );
    }

    @GetMapping
    public List< Customer > getCustomers( ) {
        return customerRepository.findAll( Sort.by( "id" ).descending( ) );
    }
}
