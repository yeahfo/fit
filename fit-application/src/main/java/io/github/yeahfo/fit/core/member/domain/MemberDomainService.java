package io.github.yeahfo.fit.core.member.domain;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.github.yeahfo.fit.common.password.PasswordEncoder;
import io.github.yeahfo.fit.core.common.domain.user.User;
import io.github.yeahfo.fit.core.common.exception.FitException;
import io.github.yeahfo.fit.core.department.domain.DepartmentRepository;
import io.github.yeahfo.fit.core.department.domain.TenantCachedDepartment;
import io.github.yeahfo.fit.core.member.domain.events.MemberDomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static io.github.yeahfo.fit.core.common.exception.ErrorCode.*;
import static io.github.yeahfo.fit.core.common.utils.MapUtils.mapOf;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Service
@RequiredArgsConstructor
public class MemberDomainService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final DepartmentRepository departmentRepository;

    public ResultWithDomainEvents< Member, MemberDomainEvent > register( String mobile, String email, String password,
                                                                         User user ) {
        return Member.register( mobile, email, passwordEncoder.encode( password ), user );
    }

    @Transactional( propagation = REQUIRES_NEW )//使用REQUIRES_NEW保证即便其他地方有异常，这里也能正常写库
    public void recordMemberFailedLogin( Member member ) {
        member.recordFailedLogin( );
        memberRepository.save( member );
    }

    public ResultWithDomainEvents< Member, MemberDomainEvent > create( String name,
                                                                       List< String > departmentIds,
                                                                       String mobile,
                                                                       String email,
                                                                       String password,
                                                                       String customId,
                                                                       User user ) {

        String tenantId = user.tenantId( );
        if ( notAllDepartmentsExist( departmentIds, tenantId ) ) {
            throw new FitException( NOT_ALL_DEPARTMENTS_EXITS, "添加成员失败，有部门不存在。", "name", name, "departmentIds", departmentIds );
        }

        if ( isBlank( mobile ) && isBlank( email ) ) {
            throw new FitException( MOBILE_EMAIL_CANNOT_BOTH_EMPTY, "添加成员失败，手机号和邮箱不能同时为空。", "tenantId", tenantId );
        }

        if ( isNotBlank( mobile ) && memberRepository.existsByMobile( mobile ) ) {
            throw new FitException( MEMBER_WITH_MOBILE_ALREADY_EXISTS, "添加成员失败，手机号已被占用。", mapOf( "mobile", mobile ) );
        }

        if ( isNotBlank( email ) && memberRepository.existsByEmail( email ) ) {
            throw new FitException( MEMBER_WITH_EMAIL_ALREADY_EXISTS, "添加成员失败，邮箱已被占用。", mapOf( "email", email ) );
        }

        if ( isNotBlank( customId ) && existsByCustomId( customId, tenantId ) ) {
            throw new FitException( MEMBER_WITH_CUSTOM_ID_ALREADY_EXISTS, "添加成员失败，自定义编号已被占用。", mapOf( "customId", customId ) );
        }
        return Member.create( name, departmentIds, mobile, email, passwordEncoder.encode( password ), customId, user );
    }

    private boolean existsByCustomId( String customId, String tenantId ) {
        List< TenantCachedMember > tenantCachedMembers = memberRepository.tenantCachedMembers( tenantId );
        return tenantCachedMembers.stream( ).anyMatch( member -> Objects.equals( member.customId( ), customId ) );
    }

    private boolean notAllDepartmentsExist( List< String > departmentIds, String tenantId ) {

        if ( isEmpty( departmentIds ) ) {
            return false;
        }
        return !departmentRepository.tenantCachedDepartments( tenantId ).stream( )
                .map( TenantCachedDepartment::id )
                .collect( Collectors.toSet( ) ).containsAll( departmentIds );
    }
}
