package io.github.yeahfo.fit.core.app.infrastructure;

import io.github.yeahfo.fit.core.app.domain.App;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppRepositoryImplementation extends MongoRepository< App, String > {
}
