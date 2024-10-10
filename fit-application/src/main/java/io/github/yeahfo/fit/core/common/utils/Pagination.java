package io.github.yeahfo.fit.core.common.utils;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import static io.github.yeahfo.fit.core.common.exception.FitException.requestValidationException;

@Getter
@EqualsAndHashCode
public class Pagination {
    private final int page;
    private final int size;

    private Pagination( int page, int size ) {
        if ( page < 1 ) {
            throw requestValidationException( "detail", "page不能小于1" );
        }

        if ( page > 10000 ) {
            throw requestValidationException( "detail", "page不能大于10000" );
        }

        if ( size > 500 ) {
            throw requestValidationException( "detail", "size不能大于500" );
        }

        this.page = page;
        this.size = size;
    }

    public static Pagination pagination( int page, int size ) {
        return new Pagination( page, size );
    }

    public int skip( ) {
        return ( this.page - 1 ) * this.size;
    }

    public int limit( ) {
        return this.size;
    }

}
