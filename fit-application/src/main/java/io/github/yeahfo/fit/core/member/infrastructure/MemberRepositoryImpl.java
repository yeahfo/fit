package io.github.yeahfo.fit.core.member.infrastructure;

import io.github.yeahfo.fit.core.common.utils.Pagination;
import io.github.yeahfo.fit.core.member.application.queries.ListMember;
import io.github.yeahfo.fit.core.member.domain.Member;
import io.github.yeahfo.fit.core.member.domain.MemberRepository;
import io.github.yeahfo.fit.core.member.domain.TenantCachedMember;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static io.github.yeahfo.fit.core.common.utils.CommonUtils.*;
import static io.github.yeahfo.fit.core.common.utils.FitConstants.*;
import static io.github.yeahfo.fit.core.common.utils.MongoCriteriaUtils.regexSearch;
import static io.github.yeahfo.fit.core.common.validation.id.member.MemberIdValidator.isMemberId;
import static java.util.regex.Pattern.matches;
import static org.apache.logging.log4j.util.Strings.isBlank;
import static org.apache.logging.log4j.util.Strings.isNotBlank;
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
    public Optional< Member > findByMobileOrEmail( String mobileOrEmail ) {
        return implementation.findByMobileOrEmail( mobileOrEmail, mobileOrEmail );
    }

    @Override
    public boolean existsByMobileOrEmail( String mobileOrEmail ) {
        requireNonBlank( mobileOrEmail, "Mobile or email must not be blank." );
        return implementation.existsByMobileOrEmail( mobileOrEmail, mobileOrEmail );
    }

    @Override
    public boolean existsByMobile( String mobile ) {
        return implementation.existsByMobile( mobile );
    }

    @Override
    public boolean existsByEmail( String email ) {
        return implementation.existsByEmail( email );
    }

    @Override
    @Cacheable( value = TENANT_MEMBERS_CACHE, key = "#tenantId" )
    public List< TenantCachedMember > tenantCachedMembers( String tenantId ) {
        requireNonBlank( tenantId, "Tenant ID must not be blank." );

        Query query = query( where( "tenantId" ).is( tenantId ) );
        query.fields( ).include( "name", "role", "mobile", "email", "mobileWxOpenId", "customId", "departmentIds", "active" );
        return mongoTemplate.find( query, TenantCachedMember.class, MEMBER_COLLECTION );
    }

    @Override
    public Page< ListMember > listTenantMembers( int page, int size, Sort sort, String tenantId, String departmentId, String search ) {
        Pagination pagination = Pagination.pagination( page, size );
        Query query = new Query( buildMemberQueryCriteria( tenantId, departmentId, search ) );
        PageRequest pageRequest = PageRequest.of( page - 1, size ).withSort( sort );
        query.skip( pagination.skip( ) ).limit( pagination.limit( ) ).with( sort );

        List< ListMember > listMembers = mongoTemplate.find( query, ListMember.class, MEMBER_COLLECTION );
        return PageableExecutionUtils.getPage(
                listMembers,
                pageRequest,
                ( ) -> mongoTemplate.count( query.skip( 0 ).limit( 0 ), Member.class ) );
    }

    private Criteria buildMemberQueryCriteria( String tenantId,
                                               String departmentId,
                                               String search ) {
        Criteria criteria = where( "tenantId" ).is( tenantId );

        if ( isNotBlank( departmentId ) ) {
            criteria.and( "departmentIds" ).is( departmentId );
        }

        //1. search为空时返回
        if ( isBlank( search ) ) {
            return criteria;
        }

        //2. 直接根据id搜索
        if ( isMemberId( search ) ) {
            return criteria.and( "_id" ).is( search );
        }

        //3. search为手机号时，精确手机号查询
        if ( isMobileNumber( search ) ) {
            return criteria.and( "mobile" ).is( search );
        }

        //4. search为邮箱时，精确邮箱查询
        if ( isEmail( search ) ) {
            return criteria.and( "email" ).is( search );
        }

        //5. 当为部分手机号时，用正则搜索MOBILE
        if ( matches( "^[0-9]{4,11}$", search ) ) {
            return criteria.and( "mobile" ).regex( search );
        }

        //6. 其他情况下，用正则搜索name或email或customId
        return criteria.orOperator( where( "customId" ).is( search ), regexSearch( "name", search ), where( "email" ).regex( search ) );
    }

    @Override
    @CacheEvict( value = TENANT_MEMBERS_CACHE, key = "#member.tenantId()" )
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
    @CacheEvict( value = MEMBER_CACHE, key = "#id" )
    public void deleteById( String id ) {
        implementation.deleteById( id );
    }

    @Override
    @Caching( evict = {
            @CacheEvict( value = MEMBER_CACHE, key = "#member.identifier()" ),
            @CacheEvict( value = TENANT_MEMBERS_CACHE, key = "#member.tenantId()" )
    } )
    public void delete( Member member ) {
        implementation.delete( member );
    }

    @Override
    public void deleteAllById( Iterable< String > ids ) {
        implementation.deleteAllById( ids );
    }
}
