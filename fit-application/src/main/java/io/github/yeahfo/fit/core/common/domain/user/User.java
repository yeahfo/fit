package io.github.yeahfo.fit.core.common.domain.user;

import static io.github.yeahfo.fit.core.common.domain.user.Role.*;
import static io.github.yeahfo.fit.core.common.exception.FitException.accessDeniedException;
import static io.github.yeahfo.fit.core.common.exception.FitException.authenticationException;
import static io.github.yeahfo.fit.core.common.utils.CommonUtils.requireNonBlank;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public record User( String memberId,
                    String name,
                    String tenantId,
                    Role role ) {
    public static final User NOUSER = new User( null, null, null, null );
    public static final User ANONYMOUS_USER = NOUSER;

    public static User humanUser( String memberId, String name, String tenantId, Role role ) {
        requireNonBlank( memberId, "MemberId must not be blank." );
        requireNonBlank( name, "Name must not be blank." );
        requireNonBlank( tenantId, "TenantId must not be blank." );
        requireNonNull( role, "Role must not be null." );

        if ( role == ROBOT ) {
            throw new IllegalStateException( "Human user should not have ROBOT role." );
        }

        return new User( memberId, name, tenantId, role );
    }

    public static User robotUser( String tenantId ) {
        requireNonBlank( tenantId, "TenantId must not be blank." );

        return new User( null, null, tenantId, ROBOT );
    }

    public boolean isLoggedIn( ) {
        return internalIsLoggedIn( );
    }

    private boolean internalIsLoggedIn( ) {
        return isNotBlank( tenantId ) && role != null;
    }

    public boolean isHumanUser( ) {
        if ( !internalIsLoggedIn( ) ) {
            return false;
        }

        return internalIsHumanUser( );
    }

    private boolean internalIsHumanUser( ) {
        return role == TENANT_ADMIN || role == TENANT_MEMBER;
    }

    public void checkIsTenantAdmin( ) {
        internalCheckLoggedIn( );
        internalCheckTenantAdmin( );
    }

    private void internalCheckLoggedIn( ) {
        if ( !internalIsLoggedIn( ) ) {
            throw authenticationException( );
        }
    }

    private void internalCheckTenantAdmin( ) {
        if ( !internalIsTenantAdmin( ) ) {
            throw accessDeniedException( );
        }
    }

    private boolean internalIsTenantAdmin( ) {
        return role == TENANT_ADMIN;
    }
}
