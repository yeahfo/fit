package io.github.yeahfo.cor.order.infrastructure;

import io.github.yeahfo.cor.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepositoryImplementation extends JpaRepository< Order, Long > {
}
