package io.github.yeahfo.fit.core.departmenthierarchy.infrastructure;

import io.github.yeahfo.fit.core.departmenthierarchy.domain.DepartmentHierarchy;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DepartmentHierarchyRepositoryImplementation extends MongoRepository< DepartmentHierarchy, String> {
}
