package io.github.yeahfo.fit.core.app.application;

import io.github.yeahfo.fit.common.ratelimit.RateLimiter;
import io.github.yeahfo.fit.core.app.domain.App;
import io.github.yeahfo.fit.core.app.domain.AppFactory;
import io.github.yeahfo.fit.core.app.domain.AppRepository;
import io.github.yeahfo.fit.core.app.domain.CreateAppResult;
import io.github.yeahfo.fit.core.common.domain.user.User;
import io.github.yeahfo.fit.core.group.domain.Group;
import io.github.yeahfo.fit.core.grouphierarchy.domain.GroupHierarchy;
import io.github.yeahfo.fit.core.tenant.domain.PackagesStatus;
import io.github.yeahfo.fit.core.tenant.domain.TenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppCommandService {
    private final AppFactory appFactory;
    private final RateLimiter rateLimiter;
    private final AppRepository appRepository;
    private final TenantRepository tenantRepository;

    @Transactional
    public CreateAppResponse createApp( CreateAppCommand command, User user ) {
        user.checkIsTenantAdmin( );
        rateLimiter.applyFor( user.tenantId( ), "App:Create", 1 );

        PackagesStatus packagesStatus = tenantRepository.packagesStatusOf( user.tenantId( ) );
        packagesStatus.validateAddApp( );

        CreateAppResult result = appFactory.create( command.name( ), user );
        App app = result.app( );
        appRepository.save( app );

        Group defaultGroup = result.defaultGroup( );
//        groupRepository.save( defaultGroup );
//
//        GroupHierarchy groupHierarchy = result.getGroupHierarchy( );
//        groupHierarchyRepository.save( groupHierarchy );
//        log.info( "Created app[{}].", app.getId( ) );

        return CreateAppResponse.builder( )
                .appId( app.identifier( ) )
                .defaultGroupId( defaultGroup.identifier( ) )
//                .homePageId( app.homePageId( ) )
                .build( );
    }
}
