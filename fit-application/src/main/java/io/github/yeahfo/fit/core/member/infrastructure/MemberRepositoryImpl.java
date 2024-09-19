package io.github.yeahfo.fit.core.member.infrastructure;

import io.github.yeahfo.fit.core.member.domain.Member;
import io.github.yeahfo.fit.core.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static io.github.yeahfo.fit.core.common.utils.CommonUtils.requireNonBlank;
import static io.github.yeahfo.fit.core.common.utils.FitConstants.MEMBER_CACHE;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;


@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {
    private final MongoTemplate mongoTemplate;
    private final MemberRepositoryImplementation implementation;


    @Override
    public long countByTenantId( String tenantId ) {
        requireNonBlank( tenantId, "Tenant ID must not be blank." );
        Query query = query( where( "tenantId" ).is( tenantId ) );
        return mongoTemplate.count( query, Member.class );
    }

    @Override
    public Member save( Member member ) {
        return implementation.save( member );
    }

    @Override
    public List< Member > saveAll( Iterable< Member > members ) {
        return implementation.saveAll( members );
    }

    @Override
    @Cacheable( value = MEMBER_CACHE, key = "#id", unless = "#result == null" )
    public Optional< Member > findById( String id ) {
        return implementation.findById( id );
    }

    @Override
    public List< Member > findAllById( Iterable< String > ids ) {
        return implementation.findAllById( ids );
    }

    @Override
    public void deleteById( String id ) {
        implementation.deleteById( id );
    }

    @Override
    public void deleteAllById( Iterable< String > ids ) {
        implementation.deleteAllById( ids );
    }
}
