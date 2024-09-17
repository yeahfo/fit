package io.github.yeahfo.fit.core.order.infrastructure;

import io.github.yeahfo.fit.core.order.domain.Order;
import io.github.yeahfo.fit.core.order.domain.OrderRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepositoryImplementation extends OrderRepository, MongoRepository< Order, Long > {
}
