package io.github.yeahfo.fit.core.member.domain;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.github.yeahfo.fit.core.common.domain.AggregateRoot;
import io.github.yeahfo.fit.core.common.domain.UploadedFile;
import io.github.yeahfo.fit.core.common.domain.user.Role;
import io.github.yeahfo.fit.core.common.domain.user.User;
import io.github.yeahfo.fit.core.common.exception.FitException;
import io.github.yeahfo.fit.core.member.domain.events.MemberCreatedEvent;
import io.github.yeahfo.fit.core.member.domain.events.MemberDepartmentsChangedEvent;
import io.github.yeahfo.fit.core.member.domain.events.MemberDomainEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static io.github.yeahfo.fit.core.common.domain.user.Role.TENANT_ADMIN;
import static io.github.yeahfo.fit.core.common.domain.user.Role.TENANT_MEMBER;
import static io.github.yeahfo.fit.core.common.exception.ErrorCode.*;
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
}
