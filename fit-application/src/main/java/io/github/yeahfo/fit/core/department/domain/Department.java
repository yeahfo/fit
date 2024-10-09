package io.github.yeahfo.fit.core.department.domain;

import io.github.yeahfo.fit.core.common.domain.AggregateRoot;

import java.util.List;

public class Department extends AggregateRoot {
    private String name;
    private List< String > managers;
    private String customId;
}
