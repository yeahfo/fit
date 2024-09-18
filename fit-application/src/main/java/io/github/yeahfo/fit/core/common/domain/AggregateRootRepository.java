package io.github.yeahfo.fit.core.common.domain;


import io.github.yeahfo.fit.core.common.exception.FitException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

import static io.github.yeahfo.fit.core.common.exception.ErrorCode.AR_NOT_FOUND;
import static io.github.yeahfo.fit.core.common.utils.CommonUtils.requireNonBlank;
import static io.github.yeahfo.fit.core.common.utils.MapUtils.mapOf;

public interface AggregateRootRepository< A extends AggregateRoot, ID > {

    A save( A aggregateRoot );

    default List< A > saveAll( Iterable< A > aggregateRoots ) {
        return Collections.emptyList( );
    }

    Optional< A > findById( ID id );

    default List< A > findAllById( Iterable< ID > ids ) {
        return Collections.emptyList( );
    }

    void deleteById( ID id );

    default void deleteAllById( Iterable< ID > ids ) {
    }

    default A find( ID id ) {
        requireNonBlank( String.valueOf( id ), "Aggregate root ID must not be blank." );
        return findById( id ).orElseThrow( ( ) -> new FitException( AR_NOT_FOUND, "未找到资源。", mapOf( "type", getType( ).getSimpleName( ), "id", id ) ) );
    }

    Map< String, Class< ? > > classMapper = new HashMap<>( );

    default Class< ? > getType( ) {
        String className = getClass( ).getSimpleName( );

        if ( !classMapper.containsKey( className ) ) {
            Type genericSuperclass = getClass( ).getGenericSuperclass( );
            Type[] actualTypeArguments = ( ( ParameterizedType ) genericSuperclass ).getActualTypeArguments( );
            classMapper.put( className, ( Class< ? > ) actualTypeArguments[ 0 ] );
        }
        return classMapper.get( className );
    }
}
