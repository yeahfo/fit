package io.github.yeahfo.fit.core.member.domain;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.github.yeahfo.fit.core.common.domain.AggregateRoot;
import io.github.yeahfo.fit.core.common.domain.UploadedFile;
import io.github.yeahfo.fit.core.common.domain.user.Role;
import io.github.yeahfo.fit.core.common.domain.user.User;
import io.github.yeahfo.fit.core.common.exception.FitException;
import io.github.yeahfo.fit.core.member.domain.events.*;

import java.util.*;
import java.util.stream.Stream;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static io.github.yeahfo.fit.core.common.domain.user.Role.TENANT_ADMIN;
import static io.github.yeahfo.fit.core.common.domain.user.Role.TENANT_MEMBER;
import static io.github.yeahfo.fit.core.common.exception.ErrorCode.*;
import static io.github.yeahfo.fit.core.common.exception.FitException.accessDeniedException;
import static io.github.yeahfo.fit.core.common.utils.MapUtils.mapOf;
import static io.github.yeahfo.fit.core.common.utils.Identified.newMemberId;
import static java.util.Set.copyOf;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class Member extends AggregateRoot {
    protected String name;
    protected String password;
    protected Role role;
    protected String email;
    protected String mobile;
    protected boolean mobileIdentified;
    protected UploadedFile avatar;
    protected String customId;
    protected List< String > topAppIds;
    protected FailedLoginCount failedLoginCount;
    protected boolean active;
    protected boolean tenantActive;
    protected List< String > departmentIds;

    public String name( ) {
        return name;
    }

    public Role role( ) {
        return role;
    }

    public String mobile( ) {
        return mobile;
    }

    public String email( ) {
        return email;
    }

    protected Member( ) {
    }

    private Member( String mobile, String email, String password, User user ) {
        super( user.memberId( ), user );
        this.name = user.name( );
        this.role = TENANT_ADMIN;
        this.mobile = mobile;
        if ( isNotBlank( this.mobile ) ) {
            this.mobileIdentified = true;
        }
        this.email = email;
        this.password = password;
        this.failedLoginCount = FailedLoginCount.init( );
        this.active = true;
        this.tenantActive = true;
        this.topAppIds = List.of( );
        this.departmentIds = List.of( );
        this.addOpsLog( "注册", user );
    }

    private Member( String name, List< String > departmentIds, String mobile, String email, String password, String customId, User user ) {
        super( newMemberId( ), user );
        this.name = name;
        this.mobile = mobile;
        this.mobileIdentified = false;
        this.email = email;
        this.password = password;
        this.customId = customId;
        this.role = TENANT_MEMBER;
        this.failedLoginCount = FailedLoginCount.init( );
        this.active = true;
        this.tenantActive = true;
        this.topAppIds = List.of( );
        this.departmentIds = isNotEmpty( departmentIds ) ? departmentIds : new ArrayList<>( 0 );
        this.addOpsLog( "新建", user );
    }

    protected static ResultWithDomainEvents< Member, MemberDomainEvent > register( String mobile,
                                                                                   String email,
                                                                                   String password,
                                                                                   User user ) {
        return new ResultWithDomainEvents<>( new Member( mobile, email, password, user ), new MemberCreatedEvent( user ) );
    }

    protected static ResultWithDomainEvents< Member, MemberDomainEvent > create( String name,
                                                                                 List< String > departmentIds,
                                                                                 String mobile,
                                                                                 String email,
                                                                                 String password,
                                                                                 String customId,
                                                                                 User user ) {
        Member member = new Member( name, departmentIds, mobile, email, password, customId, user );
        List< MemberDomainEvent > events = new ArrayList<>( );
        events.add( new MemberCreatedEvent( user ) );
        if ( isNotEmpty( departmentIds ) ) {
            events.add( new MemberDepartmentsChangedEvent( Set.of( ), copyOf( departmentIds ), user ) );
        }
        return new ResultWithDomainEvents<>( member, events );
    }

    public void checkActive( ) {
        if ( this.failedLoginCount.isLocked( ) ) {
            throw new FitException( MEMBER_ALREADY_LOCKED, "当前用户已经被锁定，次日零点系统将自动解锁。", mapOf( "memberId", this.identifier( ) ) );
        }

        if ( !this.active ) {
            throw new FitException( MEMBER_ALREADY_DEACTIVATED, "当前用户已经被禁用。", mapOf( "memberId", this.identifier( ) ) );
        }

        if ( !this.tenantActive ) {
            throw new FitException( TENANT_ALREADY_DEACTIVATED, "当前账户已经被禁用。",
                    mapOf( "memberId", this.identifier( ), "tenantId", this.tenantId( ) ) );
        }
    }

    public User toUser( ) {
        return User.humanUser( id, name, tenantId, role );
    }

    public String password( ) {
        return password;
    }

    public void recordFailedLogin( ) {
        this.failedLoginCount.recordFailedLogin( );
    }

    public ResultWithDomainEvents< Member, MemberDomainEvent > update( String name, List< String > departmentIds, String mobile, String email, User user ) {
        List< MemberDomainEvent > events = new ArrayList<>( );
        if ( !Objects.equals( this.name, name ) ) {
            this.name = name;
            events.add( new MemberNameChangedEvent( name, user ) );
        }

        if ( !Objects.equals( this.mobile, mobile ) ) {
            this.mobileIdentified = false;
        }

        this.mobile = mobile;
        this.email = email;

        if ( departmentIds != null ) {//传入null时，不做任何departmentIds的更新，主要用于不因为null而将已有的departmentIds更新没了
            Set< String > removedDepartmentIds = diff( this.departmentIds, departmentIds );
            Set< String > addedDepartmentIds = diff( departmentIds, this.departmentIds );
            if ( isNotEmpty( removedDepartmentIds ) || isNotEmpty( addedDepartmentIds ) ) {
                events.add( new MemberDepartmentsChangedEvent( removedDepartmentIds, addedDepartmentIds, user ) );
            }
            this.departmentIds = departmentIds;
        }

        this.addOpsLog( "更新信息", user );
        return new ResultWithDomainEvents<>( this, events );
    }

    private Set< String > diff( List< String > list1, List< String > list2 ) {
        HashSet< String > result = new HashSet<>( list1 );
        result.removeAll( new HashSet<>( list2 ) );
        return result;
    }

    public void isTenantOwned( String operatorTenantId ) {
        if ( !this.tenantId.equals( operatorTenantId ) ) {
            throw accessDeniedException( );
        }
    }

    public void updateRole( Role role, User user ) {
        isTenantOwned( user.tenantId( ) );
        this.role = role;
        this.addOpsLog( "更新角色为" + role.getRoleName( ), user );
    }

    public ResultWithDomainEvents< Member, MemberDomainEvent > delete( User user ) {
        isTenantOwned( user.tenantId( ) );
        return new ResultWithDomainEvents<>( this, new MemberDeletedEvent( user ) );
    }

    public void activate( User user ) {
        isTenantOwned( user.tenantId( ) );
        if ( active ) {
            return;
        }

        this.active = true;
        addOpsLog( "启用", user );
    }

    public void deactivate( User user ) {
        isTenantOwned( user.tenantId( ) );
        if ( !active ) {
            return;
        }

        this.active = false;
        addOpsLog( "禁用", user );
    }

    public void changePassword( String encoded, User user ) {
        this.password = encoded;
        this.addOpsLog( "重置密码", user );
    }

    public void changeMobile( String newMobile, User user ) {
        if ( Objects.equals( this.mobile, newMobile ) ) {
            return;
        }

        this.mobile = newMobile;
        this.mobileIdentified = true;
        this.addOpsLog( "修改手机号为[" + mobile + "]", user );
    }

    public void identifyMobile( String mobile, User user ) {
        if ( isNotBlank( this.mobile ) && !Objects.equals( this.mobile, mobile ) ) {
            throw new FitException( IDENTIFY_MOBILE_NOT_THE_SAME, "认证手机号与您当前账号的手机号不一致，无法完成认证。", "mobile", mobile );
        }

        this.mobile = mobile;
        this.mobileIdentified = true;
        this.addOpsLog( "认证手机号：" + mobile, user );
    }

    public ResultWithDomainEvents< Member, MemberDomainEvent > updateBaseSetting( String name, User user ) {
        if ( Objects.equals( this.name, name ) ) {
            return new ResultWithDomainEvents<>( this );
        }

        this.name = name;

        this.addOpsLog( "更新基本设置", user );
        return new ResultWithDomainEvents<>( this, new MemberNameChangedEvent( name, user ) );
    }

    public void updateAvatar( UploadedFile avatar, User user ) {
        this.avatar = avatar;
        addOpsLog( "更新头像", user );
    }

    public void deleteAvatar( User user ) {
        this.avatar = null;
        addOpsLog( "删除头像", user );
    }

    public void topApp( String appId, User user ) {
        topAppIds = Stream.concat( Stream.of( appId ), this.topAppIds.stream( ) ).limit( 20 ).collect( toImmutableList( ) );
        addOpsLog( "顶置应用[" + appId + "]", user );
    }

    public void cancelTopApp( String appId, User user ) {
        this.topAppIds = this.topAppIds.stream( ).filter( id -> !Objects.equals( id, appId ) ).collect( toImmutableList( ) );
        addOpsLog( "取消顶置应用[" + appId + "]", user );
    }

    public UploadedFile avatar( ) {
        return avatar;
    }

    public List< String > toppedAppIds( ) {
        return topAppIds;
    }

    public boolean mobileIdentified( ) {
        return mobileIdentified;
    }

    public List< String > departmentIds( ) {
        return departmentIds;
    }
}
