package io.github.yeahfo.fit.core.register.domain;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.github.yeahfo.fit.core.common.domain.user.User;
import io.github.yeahfo.fit.core.common.exception.FitException;
import io.github.yeahfo.fit.core.departmenthierarchy.domain.DepartmentHierarchy;
import io.github.yeahfo.fit.core.departmenthierarchy.domain.events.DepartmentHierarchyDomainEvent;
import io.github.yeahfo.fit.core.member.domain.Member;
import io.github.yeahfo.fit.core.member.domain.MemberDomainService;
import io.github.yeahfo.fit.core.member.domain.MemberRepository;
import io.github.yeahfo.fit.core.member.domain.events.MemberDomainEvent;
import io.github.yeahfo.fit.core.tenant.domain.CreateTenantHolder;
import io.github.yeahfo.fit.core.tenant.domain.Tenant;
import io.github.yeahfo.fit.core.tenant.domain.events.TenantDomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

import static io.github.yeahfo.fit.core.common.exception.ErrorCode.MEMBER_WITH_MOBILE_OR_EMAIL_ALREADY_EXISTS;
import static io.github.yeahfo.fit.core.common.utils.CommonUtils.isMobileNumber;
import static io.github.yeahfo.fit.core.common.utils.CommonUtils.maskMobileOrEmail;

@Component
@RequiredArgsConstructor
public class RegisterDomainService {
    private final MemberRepository memberRepository;
    private final MemberDomainService memberDomainService;

    public CreateTenantHolder register( String mobileOrEmail, String password, String tenantName, User user,
                                        Consumer< ResultWithDomainEvents< Tenant, TenantDomainEvent > > tenantCreatedConsumer,
                                        Consumer< ResultWithDomainEvents< Member, MemberDomainEvent > > memberCreatedConsumer,
                                        Consumer< ResultWithDomainEvents< DepartmentHierarchy, DepartmentHierarchyDomainEvent > > dHCreatedConsumer ) {
        if ( memberRepository.existsByMobileOrEmail( mobileOrEmail ) ) {
            throw new FitException( MEMBER_WITH_MOBILE_OR_EMAIL_ALREADY_EXISTS, "注册失败，手机号或邮箱已被占用。",
                    "mobileOrEmail", maskMobileOrEmail( mobileOrEmail ) );
        }
        String mobile = null;
        String email = null;
        if ( isMobileNumber( mobileOrEmail ) ) {
            mobile = mobileOrEmail;
        } else {
            email = mobileOrEmail;
        }
        ResultWithDomainEvents< Tenant, TenantDomainEvent > tenantCreated = Tenant.create( tenantName, user );
        ResultWithDomainEvents< Member, MemberDomainEvent > memberRegistered = memberDomainService.register( mobile, email, password, user );
        ResultWithDomainEvents< DepartmentHierarchy, DepartmentHierarchyDomainEvent > departmentHierarchyCreated = DepartmentHierarchy.create( user );
        tenantCreatedConsumer.accept( tenantCreated );
        memberCreatedConsumer.accept( memberRegistered );
        dHCreatedConsumer.accept( departmentHierarchyCreated );

        return CreateTenantHolder.builder( )
                .tenant( tenantCreated.result )
                .member( memberRegistered.result )
                .departmentHierarchy( departmentHierarchyCreated.result )
                .build( );
    }
}
