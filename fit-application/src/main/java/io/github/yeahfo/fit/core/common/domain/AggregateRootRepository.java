package io.github.yeahfo.fit.core.common.domain;

import java.util.List;
import java.util.Optional;

public interface AggregateRootRepository< A extends AggregateRoot, ID > {

    A save( A aggregateRoot );

    List< AggregateRoot > saveAll( Iterable< AggregateRoot > aggregateRoots );

    Optional< A > findById( ID id );

    List< A > findAllById( Iterable< ID > ids );

    boolean existsById( ID id );

    void deleteById( ID id );

    void delete( A aggregateRoot );

    void deleteAllById( Iterable< ID > ids );

    void deleteAll( Iterable< AggregateRoot > aggregateRoots );
//    default A findByOne( Identifier identifier ) {
//        requireNonBlank( String.valueOf( identifier ), "AR ID must not be blank." );
//        return findById( id ).orElseThrow( ( ) -> new MryException( AR_NOT_FOUND, "未找到资源。", mapOf( "type", getType( ).getSimpleName( ), "id", id ) ) );
//    }
}
