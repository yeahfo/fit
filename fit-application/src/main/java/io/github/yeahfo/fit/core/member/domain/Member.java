package io.github.yeahfo.fit.core.member.domain;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.github.yeahfo.fit.core.common.domain.AggregateRoot;
import io.github.yeahfo.fit.core.common.domain.UploadedFile;
import io.github.yeahfo.fit.core.common.domain.user.Role;
import io.github.yeahfo.fit.core.common.domain.user.User;
import io.github.yeahfo.fit.core.member.domain.events.MemberCreatedEvent;
import io.github.yeahfo.fit.core.member.domain.events.MemberDomainEvent;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

import static io.github.yeahfo.fit.core.common.domain.user.Role.TENANT_ADMIN;
import static java.time.LocalDate.now;
import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class Member extends AggregateRoot {
    @Getter
    @Builder
    @EqualsAndHashCode
    @AllArgsConstructor( access = PRIVATE )
    public static class FailedLoginCount {
        private static final int MAX_ALLOWED_FAILED_LOGIN_PER_DAY = 30;

        private LocalDate date;
        private int count;

        public static FailedLoginCount init( ) {
            return FailedLoginCount.builder( ).date( now( ) ).count( 0 ).build( );
        }

        private void recordFailedLogin( ) {
            LocalDate now = now( );
            if ( now.equals( date ) ) {
                count++;
            } else {
                this.date = now;
                this.count = 0;
            }
        }

        private boolean isLocked( ) {
            return now( ).equals( date ) && this.count >= MAX_ALLOWED_FAILED_LOGIN_PER_DAY;
        }
    }

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

    public static ResultWithDomainEvents< Member, MemberDomainEvent > register( String mobile, String email, String password, User user ) {
        return new ResultWithDomainEvents<>( new Member( mobile, email, password, user ), new MemberCreatedEvent( user ) );
    }
}
