package io.github.yeahfo.cor.order.history.infrastructure;

import io.github.yeahfo.cor.order.history.domain.CustomerView;
import org.springframework.data.keyvalue.repository.KeyValueRepository;

public interface CustomerViewRepositoryImplementation extends KeyValueRepository< CustomerView, Long > {
}
