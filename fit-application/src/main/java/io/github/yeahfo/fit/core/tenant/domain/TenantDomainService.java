package io.github.yeahfo.fit.core.tenant.domain;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.github.yeahfo.fit.core.common.domain.user.User;
import io.github.yeahfo.fit.core.common.exception.FitException;
import io.github.yeahfo.fit.core.tenant.domain.events.TenantDomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static io.github.yeahfo.fit.core.common.exception.ErrorCode.TENANT_WITH_SUBDOMAIN_PREFIX_ALREADY_EXISTS;
import static io.github.yeahfo.fit.core.common.utils.MapUtils.mapOf;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
@RequiredArgsConstructor
public class TenantDomainService {
    private final TenantRepository tenantRepository;

    public ResultWithDomainEvents< Tenant, TenantDomainEvent > updateSubdomain( Tenant tenant, String subdomainPrefix, User user ) {
        if ( isNotBlank( subdomainPrefix )
                && !Objects.equals( tenant.getSubdomainPrefix( ), subdomainPrefix )
                && tenantRepository.existsBySubdomainPrefix( subdomainPrefix ) ) {
            throw new FitException( TENANT_WITH_SUBDOMAIN_PREFIX_ALREADY_EXISTS,
                    "更新失败，域名已经被占用。", mapOf( "subdomainPrefix", subdomainPrefix ) );
        }
        return tenant.updateSubdomain( subdomainPrefix, user );
    }
}
