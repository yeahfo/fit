package io.github.yeahfo.fit.core.member.domain;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.github.yeahfo.fit.common.password.PasswordEncoder;
import io.github.yeahfo.fit.core.common.domain.user.User;
import io.github.yeahfo.fit.core.common.exception.FitException;
import io.github.yeahfo.fit.core.department.domain.DepartmentRepository;
import io.github.yeahfo.fit.core.department.domain.TenantCachedDepartment;
import io.github.yeahfo.fit.core.member.domain.events.MemberDomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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

    public ResultWithDomainEvents< Member, MemberDomainEvent > updateMember( Member member, String name, List< String > departmentIds, String mobile, String email, User user ) {
        if ( !Objects.equals( member.tenantId( ), user.tenantId( ) ) ) {
            throw new FitException( ACCESS_DENIED, "更新成员失败，该成员不属于您。",
                    mapOf( "memberId", member.identifier( ) ) );
        }
        if ( notAllDepartmentsExist( departmentIds, member.tenantId( ) ) ) {
            throw new FitException( NOT_ALL_DEPARTMENTS_EXITS, "更新成员失败，有部门不存在。", "name", name, "departmentIds", departmentIds );
        }

        if ( isBlank( mobile ) && isBlank( email ) ) {
            throw new FitException( MOBILE_EMAIL_CANNOT_BOTH_EMPTY, "更新成员失败，手机号和邮箱不能同时为空。",
                    "memberId", member.identifier( ) );
        }

        if ( isNotBlank( mobile ) && !mobile.equals( member.mobile( ) ) && memberRepository.existsByMobile( mobile ) ) {
            throw new FitException( MEMBER_WITH_MOBILE_ALREADY_EXISTS, "更新成员失败，手机号已被占用。",
                    mapOf( "memberId", member.identifier( ) ) );
        }

        if ( isNotBlank( email ) && !email.equals( member.email( ) ) && memberRepository.existsByEmail( email ) ) {
            throw new FitException( MEMBER_WITH_EMAIL_ALREADY_EXISTS, "更新成员失败，邮箱已被占用。",
                    mapOf( "memberId", member.identifier( ) ) );
        }

        return member.update( name, departmentIds, mobile, email, user );
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

    public void checkMinTenantAdminLimit( String tenantId ) {
        long count = memberRepository.tenantCachedMembers( tenantId )
                .stream( )
                .filter( member -> member.isTenantAdmin( ) && member.active( ) )
                .count( );
        if ( count < 1 ) {
            throw new FitException( NO_ACTIVE_TENANT_ADMIN_LEFT, "必须保留至少一个可用的系统管理员。",
                    mapOf( "tenantId", tenantId ) );
        }
    }

    public void checkMaxTenantAdminLimit( String tenantId ) {
        long count = memberRepository.tenantCachedMembers( tenantId )
                .stream( )
                .filter( TenantCachedMember::isTenantAdmin )
                .count( );
        if ( count > 10 ) {
            throw new FitException( MAX_TENANT_ADMIN_REACHED, "系统管理员数量已超出最大限制（10名）。",
                    mapOf( "tenantId", tenantId ) );
        }
    }
}
