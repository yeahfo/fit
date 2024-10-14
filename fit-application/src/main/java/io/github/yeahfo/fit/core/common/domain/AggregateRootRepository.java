package io.github.yeahfo.fit.core.common.domain;


import io.github.yeahfo.fit.core.common.exception.FitException;

import java.lang.reflect.ParameterizedType;
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

    default void delete( A aggregateRoot ) {

    }

    default void deleteAllById( Iterable< ID > ids ) {
    }

    default A findPresent( ID id ) {
        requireNonBlank( String.valueOf( id ), "Aggregate root ID must not be blank." );
        return this.findById( id ).orElseThrow( ( ) -> new FitException( AR_NOT_FOUND, "未找到资源。", mapOf( "type", getType( ).getSimpleName( ), "id", id ) ) );
    }

    Map< String, Class< ? > > classMapper = new HashMap<>( );

    default Class< ? > getType( ) {
        String className = getClass( ).getSimpleName( );

        if ( !classMapper.containsKey( className ) ) {
            Class< ? > actualTypeArgument = Arrays.stream( getClass( ).getGenericInterfaces( ) )
                    .filter( type -> type instanceof ParameterizedType )
                    .map( type -> ( ( ParameterizedType ) type ).getActualTypeArguments( ) )
                    .filter( types -> Arrays.stream( types ).findFirst( ).isPresent( ) )
                    .map( types -> ( Class< ? > ) Arrays.stream( types ).findFirst( ).get( ) )
                    .findFirst( ).orElseThrow( );
            classMapper.put( className, actualTypeArgument );
        }
        return classMapper.get( className );
    }
}
