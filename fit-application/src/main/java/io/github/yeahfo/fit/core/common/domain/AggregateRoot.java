package io.github.yeahfo.fit.core.common.domain;

import io.github.yeahfo.fit.core.common.domain.user.User;
import io.github.yeahfo.fit.core.common.utils.Identified;
import lombok.Setter;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

import static io.github.yeahfo.fit.core.common.utils.CommonUtils.requireNonBlank;
import static java.time.Instant.now;
import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;

public abstract class AggregateRoot implements Identified< String > {
    private static final int MAX_OPS_LOG_SIZE = 20;
    protected String id;
    protected String tenantId;
    protected Instant createdAt;
    protected String createdBy;
    protected String creator;
    protected Instant updatedAt;
    protected String updatedBy;
    protected String updater;
    protected LinkedList< OpsLog > opsLogs;


    /**
     * Version number, implementing optimistic locking.
     */
    @Setter( PRIVATE )
    protected Long version;

    @SuppressWarnings( "unused" )
    protected AggregateRoot( ) {

    }

    @Override
    public String identifier( ) {
        return id;
    }

    public String tenantId( ) {
        return tenantId;
    }

    public Instant createdAt( ) {
        return createdAt;
    }

    public String createdBy( ) {
        return createdBy;
    }

    public String creator( ) {
        return creator;
    }

    public LinkedList< OpsLog > opsLogs( ) {
        return opsLogs;
    }

    protected AggregateRoot( String id, User user ) {
        requireNonBlank( id, "ID must not be blank." );
        requireNonNull( user, "User must not be null." );
        requireNonBlank( user.tenantId( ), "Tenant ID must not be blank." );

        this.id = id;
        this.tenantId = user.tenantId( );
        this.createdAt = now( );
        this.createdBy = user.memberId( );
        this.creator = user.name( );
    }

    protected AggregateRoot( String id, String tenantId, User user ) {
        requireNonBlank( id, "AR ID must not be blank." );
        requireNonBlank( tenantId, "Tenant ID must not be blank." );
        requireNonNull( user, "User must not be null." );

        this.id = id;
        this.tenantId = tenantId;
        this.createdAt = now( );
        this.createdBy = user.memberId( );
        this.creator = user.name( );
    }

    protected void addOpsLog( String note, User user ) {
        if ( user.isLoggedIn( ) ) {
            OpsLog log = OpsLog.builder( ).note( note ).operatedAt( now( ) ).operatedBy( user.memberId( ) ).operator( user.name( ) ).build( );
            List< OpsLog > opsLogs = allOpsLogs( );

            opsLogs.add( log );
            if ( opsLogs.size( ) > MAX_OPS_LOG_SIZE ) {//最多保留最近100条
                this.opsLogs.remove( );
            }

            this.updatedAt = now( );
            this.updatedBy = user.memberId( );
            this.updater = user.name( );
        }
    }

    private List< OpsLog > allOpsLogs( ) {
        if ( opsLogs == null ) {
            this.opsLogs = new LinkedList<>( );
        }

        return opsLogs;
    }
}
