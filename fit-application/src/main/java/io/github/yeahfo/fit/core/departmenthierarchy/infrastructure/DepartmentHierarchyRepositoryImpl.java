package io.github.yeahfo.fit.core.departmenthierarchy.infrastructure;

import io.github.yeahfo.fit.core.departmenthierarchy.domain.DepartmentHierarchy;
import io.github.yeahfo.fit.core.departmenthierarchy.domain.DepartmentHierarchyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DepartmentHierarchyRepositoryImpl implements DepartmentHierarchyRepository {
    private final DepartmentHierarchyRepositoryImplementation implementation;

    @Override
    public DepartmentHierarchy save( DepartmentHierarchy departmentHierarchy ) {
        return implementation.save( departmentHierarchy );
    }

    @Override
    public Optional< DepartmentHierarchy > findById( String id ) {
        return implementation.findById( id );
    }

    @Override
    public void deleteById( String id ) {
        implementation.deleteById( id );
    }
}
