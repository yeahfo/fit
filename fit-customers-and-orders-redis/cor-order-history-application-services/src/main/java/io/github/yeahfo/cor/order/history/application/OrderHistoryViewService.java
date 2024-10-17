package io.github.yeahfo.cor.order.history.application;

import io.github.yeahfo.cor.common.domain.Money;
import io.github.yeahfo.cor.order.history.domain.CustomerView;
import io.github.yeahfo.cor.order.history.domain.CustomerViewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class OrderHistoryViewService {
    private final CustomerViewRepository customerViewRepository;

    @Transactional
    public void createCustomer( Long customerId, String customerName, Money creditLimit ) {
        CustomerView customer = customerViewRepository.save( new CustomerView( customerId, customerName, creditLimit ) );
        log.info( "The customer {} creation has been synchronized.", customer );
    }

    @Transactional
    public void addOrder( Long customerId, Long orderId, Money orderTotal ) {
        getCustomer( customerId ).ifPresent( customer -> {
            customerViewRepository.save( customer.addOrder( orderId, orderTotal ) );
            log.info( "The customer order {} has been added.", customer.orders( ).get( orderId ) );
        } );
    }

    @Transactional
    public void approveOrder( Long customerId, Long orderId ) {
        getCustomer( customerId ).ifPresent( customer -> {
            customer.approveOrder( orderId );
            this.customerViewRepository.save( customer );
        } );
    }

    @Transactional
    public void rejectOrder( Long customerId, Long orderId ) {
        getCustomer( customerId ).ifPresent( customer -> {
            customer.rejectOrder( orderId );
            this.customerViewRepository.save( customer );
        } );
    }

    public Optional< CustomerView > getCustomer( Long customerId ) {
        Optional< CustomerView > finder = customerViewRepository.findById( customerId );
        if ( finder.isEmpty( ) ) {
            log.warn( "The customer {} is not found.", customerId );
        }
        return finder;
    }
}
