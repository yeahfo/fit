package io.github.yeahfo.fit.core.departmenthierarchy.domain;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.github.yeahfo.fit.core.common.domain.AggregateRoot;
import io.github.yeahfo.fit.core.common.domain.idnode.IdTree;
import io.github.yeahfo.fit.core.common.domain.idnode.IdTreeHierarchy;
import io.github.yeahfo.fit.core.common.domain.idnode.exception.IdNodeLevelOverflowException;
import io.github.yeahfo.fit.core.common.domain.user.User;
import io.github.yeahfo.fit.core.common.exception.FitException;
import io.github.yeahfo.fit.core.departmenthierarchy.domain.events.DepartmentHierarchyDomainEvent;

import java.util.ArrayList;

import static io.github.yeahfo.fit.core.common.exception.ErrorCode.DEPARTMENT_HIERARCHY_TOO_DEEP;
import static io.github.yeahfo.fit.core.common.utils.FitConstants.MAX_GROUP_HIERARCHY_LEVEL;
import static io.github.yeahfo.fit.core.common.utils.Identified.newDepartmentHierarchyId;

public class DepartmentHierarchy extends AggregateRoot {
    protected IdTree idTree;
    protected IdTreeHierarchy hierarchy;

    private DepartmentHierarchy( User user ) {
        super( newDepartmentHierarchyId( ), user );
        this.idTree = new IdTree( new ArrayList<>( 0 ) );
        this.buildHierarchy( );
        addOpsLog( "新建", user );
    }

    public static ResultWithDomainEvents< DepartmentHierarchy, DepartmentHierarchyDomainEvent > create( User user ) {
        return new ResultWithDomainEvents<>( new DepartmentHierarchy( user ) );
    }

    private void buildHierarchy( ) {
        try {
            this.hierarchy = this.idTree.buildHierarchy( MAX_GROUP_HIERARCHY_LEVEL );//深度与group保持相同，因为可能要同步到group
        } catch ( IdNodeLevelOverflowException ex ) {
            throw new FitException( DEPARTMENT_HIERARCHY_TOO_DEEP, "部门层级最多不能超过5层。", "tenantId", this.getTenantId( ) );
        }
    }
}
