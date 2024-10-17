package io.github.yeahfo.cor.order.history.domain;

import java.util.Optional;

public interface CustomerViewRepository {
    CustomerView save( CustomerView customerView );

    Optional< CustomerView > findById( long id );
}
