package io.github.yeahfo.fit.core.department.infrastructure;

import io.github.yeahfo.fit.core.department.domain.Department;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DepartmentRepositoryImplementation extends MongoRepository< Department, String > {
}
