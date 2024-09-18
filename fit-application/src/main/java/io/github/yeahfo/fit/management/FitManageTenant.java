package io.github.yeahfo.fit.management;

import io.github.yeahfo.fit.core.common.domain.user.User;
import io.github.yeahfo.fit.core.departmenthierarchy.application.DepartmentHierarchyCommandService;
import io.github.yeahfo.fit.core.member.application.MemberCommandService;
import io.github.yeahfo.fit.core.tenant.application.CreateTenantCommand;
import io.github.yeahfo.fit.core.tenant.application.TenantCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static io.github.yeahfo.fit.core.common.domain.user.Role.TENANT_ADMIN;
import static io.github.yeahfo.fit.core.plan.domain.PlanType.FLAGSHIP;
import static java.time.ZoneId.systemDefault;

@Slf4j
@Component
@RequiredArgsConstructor
public class FitManageTenant {
    public static final String FIT_MANAGE_TENANT_ID = "TNT000000000000000001";
    public static final String ADMIN_MEMBER_ID = "MBR000000000000000001";
    public static final String ADMIN_MEMBER_NAME = "BOOS";
    public static final String ADMIN_INIT_MOBILE = "18888888888";
    public static final String ADMIN_INIT_PASSWORD = "12345678";
    public static final User FIT_MANAGE_ROBOT_USER = User.robotUser( FIT_MANAGE_TENANT_ID );
    private final TenantCommandService tenantCommandService;
    private final MemberCommandService memberCommandService;
    private final DepartmentHierarchyCommandService departmentHierarchyCommandService;

    @Transactional
    public void init( ) {
        if ( tenantCommandService.exists( FIT_MANAGE_TENANT_ID ) ) {
            return;
        }
        User user = User.humanUser( ADMIN_MEMBER_ID, ADMIN_MEMBER_NAME, FIT_MANAGE_TENANT_ID, TENANT_ADMIN );
        tenantCommandService.createTenant( CreateTenantCommand.builder( ).name( "自营" ).user( user ).build( ) );
        tenantCommandService.updateTenantPlanType( FIT_MANAGE_TENANT_ID,
                FLAGSHIP,
                LocalDate.of( 2099, 12, 31 ).atStartOfDay( systemDefault( ) ).toInstant( ),
                user );
        memberCommandService.registerMember( ADMIN_INIT_MOBILE, null, ADMIN_INIT_PASSWORD, user );
        departmentHierarchyCommandService.create( user );
        log.info( "Created FIT management tenant." );
    }
}
