package io.github.yeahfo.cor.order.history.domain;

import io.github.yeahfo.cor.common.domain.Money;

import java.util.HashMap;
import java.util.Map;

public class CustomerView {
    private Long id;
    private Map< Long, OrderInfo > orders = new HashMap<>( );
    private String name;
    private Money creditLimit;

    public CustomerView( Long id, String name, Money creditLimit ) {
        this.id = id;
        this.name = name;
        this.creditLimit = creditLimit;
    }
}
