package io.github.yeahfo.cor.common.domain;

import io.github.yeahfo.fit.common.utils.Identified;

import java.time.Instant;
import java.util.function.Supplier;

import static java.time.Instant.now;

public abstract class AggregateRoot< ID > implements Identified< ID > {
    protected ID id;
    protected Long version;
    protected Instant createdAt;
    protected ID createdBy;
    protected String creator;
    protected Instant updatedAt;
    protected ID updatedBy;
    protected String updater;

    @Override
    public ID identifier( ) {
        return this.id;
    }

    protected abstract Supplier< ID > idGenerator( );

    protected AggregateRoot( ) {
        this.id = idGenerator( ).get( );
        this.createdAt = this.updatedAt = now( );
    }

    protected void initialize( ID id, ID createdBy, String creator ) {
        this.id = id;
        this.createdAt = this.updatedAt = now( );
        this.createdBy = this.updatedBy = createdBy;
        this.creator = this.updater = creator;
    }

    protected void initialize( ID createdBy, String creator ) {
        this.id = idGenerator( ).get( );
        this.createdAt = this.updatedAt = now( );
        this.createdBy = this.updatedBy = createdBy;
        this.creator = this.updater = creator;
    }

}
