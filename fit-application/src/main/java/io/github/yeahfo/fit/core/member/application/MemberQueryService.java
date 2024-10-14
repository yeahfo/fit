package io.github.yeahfo.fit.core.member.application;

import io.github.yeahfo.fit.common.ratelimit.RateLimiter;
import io.github.yeahfo.fit.core.app.domain.AppRepository;
import io.github.yeahfo.fit.core.common.domain.user.User;
import io.github.yeahfo.fit.core.common.exception.ErrorCode;
import io.github.yeahfo.fit.core.common.exception.FitException;
import io.github.yeahfo.fit.core.department.domain.DepartmentRepository;
import io.github.yeahfo.fit.core.department.domain.TenantCachedDepartment;
import io.github.yeahfo.fit.core.departmenthierarchy.domain.DepartmentHierarchy;
import io.github.yeahfo.fit.core.departmenthierarchy.domain.DepartmentHierarchyRepository;
import io.github.yeahfo.fit.core.member.application.commands.ListMyManagedMembersCommand;
import io.github.yeahfo.fit.core.member.application.queries.*;
import io.github.yeahfo.fit.core.member.domain.Member;
import io.github.yeahfo.fit.core.member.domain.MemberReference;
import io.github.yeahfo.fit.core.member.domain.MemberRepository;
import io.github.yeahfo.fit.core.member.domain.TenantCachedMember;
import io.github.yeahfo.fit.core.tenant.application.ConsoleTenantProfile;
import io.github.yeahfo.fit.core.tenant.application.PackagesStatusInfo;
import io.github.yeahfo.fit.core.tenant.domain.Packages;
import io.github.yeahfo.fit.core.tenant.domain.PackagesStatus;
import io.github.yeahfo.fit.core.tenant.domain.Tenant;
import io.github.yeahfo.fit.core.tenant.domain.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static io.github.yeahfo.fit.core.common.utils.CommonUtils.maskMobile;
import static io.github.yeahfo.fit.core.common.utils.FitConstants.CHINESE_COLLATOR;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.logging.log4j.util.Strings.isBlank;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.domain.Sort.by;

@Service
@RequiredArgsConstructor
public class MemberQueryService {
    private final static Set< String > ALLOWED_SORT_FIELDS = Set.of( "name", "active" );
    private final RateLimiter rateLimiter;
    private final AppRepository appRepository;
    private final MemberRepository memberRepository;
    private final TenantRepository tenantRepository;
    private final DepartmentRepository departmentRepository;
    private final DepartmentHierarchyRepository departmentHierarchyRepository;

    public Page< ListMember > listMyManagedMembers( ListMyManagedMembersCommand command, User user ) {
        rateLimiter.applyFor( user.tenantId( ), "Member:List", 10 );
        return memberRepository.listTenantMembers( command.page( ), command.size( ), sort( command.sortedBy( ), command.ascSort( ) ),
                user.tenantId( ), command.departmentId( ), command.search( ) );
    }

    private Sort sort( String sortedBy, boolean ascSort ) {

        if ( isBlank( sortedBy ) || !ALLOWED_SORT_FIELDS.contains( sortedBy ) ) {
            return by( DESC, "createdAt" );
        }

        Sort.Direction direction = ascSort ? ASC : DESC;
        if ( Objects.equals( sortedBy, "createdAt" ) ) {
            return by( direction, "createdAt" );
        }

        return by( direction, sortedBy ).and( by( DESC, "createdAt" ) );
    }

    public ConsoleMemberProfile fetchMyProfile( User user ) {
        rateLimiter.applyFor( user.tenantId( ), "Member:FetchMyProfile", 100 );

        String memberId = user.memberId( );
        String tenantId = user.tenantId( );

        Member member = memberRepository.findById( memberId ).orElseThrow( ( ) -> new FitException( ErrorCode.MEMBER_NOT_FOUND, "成员未找到" ) );
        Tenant tenant = tenantRepository.findPresent( tenantId );

        boolean hasManagedApps;
        if ( user.isTenantAdmin( ) ) {
            hasManagedApps = true;
        } else {
            hasManagedApps = appRepository.existsByTenantIdAndManagers( tenantId, user.memberId( ) );
        }

        Packages packages = tenant.getPackages( );
        PackagesStatus packagesStatus = tenant.packagesStatus( );

        PackagesStatusInfo planProfile = PackagesStatusInfo.builder( )
                .planName( packages.currentPlan( ).name( ) )
                .planType( packages.currentPlanType( ) )
                .effectivePlanName( packages.effectivePlan( ).name( ) )
                .effectivePlanType( packages.effectivePlanType( ) )
                .maxAppReached( packagesStatus.isMaxAppReached( ) )
                .maxMemberReached( packagesStatus.isMaxMemberReached( ) )
                .submissionNotifyAllowed( packages.submissionNotifyAllowed( ) )
                .batchImportQrAllowed( packages.batchImportQrAllowed( ) )
                .batchImportMemberAllowed( packages.batchImportMemberAllowed( ) )
                .submissionApprovalAllowed( packages.submissionApprovalAllowed( ) )
                .reportingAllowed( packages.reportingAllowed( ) )
                .customSubdomainAllowed( packages.customSubdomainAllowed( ) )
                .developerAllowed( packages.developerAllowed( ) )
                .customLogoAllowed( packages.customLogoAllowed( ) )
                .videoAudioAllowed( packages.videoAudioAllowed( ) )
                .assignmentAllowed( packages.assignmentAllowed( ) )
                .supportedControlTypes( packages.effectiveSupportedControlTypes( ) )
                .expired( packages.isExpired( ) )
                .expireAt( packages.expireAt( ) )
                .build( );

        ConsoleTenantProfile tenantProfile = ConsoleTenantProfile.builder( )
                .tenantId( tenantId )
                .name( tenant.getName( ) )
                .subdomainPrefix( tenant.getSubdomainPrefix( ) )
                .baseDomainName( "commonProperties.getBaseDomainName( )" )
                .subdomainReady( tenant.isSubdomainReady( ) )
                .logo( tenant.getLogo( ) )
                .packagesStatus( planProfile )
                .build( );

        return ConsoleMemberProfile.builder( )
                .memberId( memberId )
                .tenantId( member.tenantId( ) )
                .name( member.name( ) )
                .role( member.role( ) )
                .avatar( member.avatar( ) )
                .hasManagedApps( hasManagedApps )
                .tenantProfile( tenantProfile )
                .topAppIds( member.toppedAppIds( ) )
                .mobileIdentified( member.mobileIdentified( ) )
                .build( );
    }

    public ClientMemberProfile fetchMyClientMemberProfile( User user ) {
        rateLimiter.applyFor( user.tenantId( ), "Member:FetchMyClientProfile", 100 );

        String memberId = user.memberId( );
        String tenantId = user.tenantId( );

        Member member = memberRepository.findById( memberId ).orElseThrow( ( ) -> new FitException( ErrorCode.MEMBER_NOT_FOUND, "成员未找到" ) );
        Tenant tenant = tenantRepository.findById( tenantId ).orElseThrow( ( ) -> new FitException( ErrorCode.TENANT_NOT_FOUND, "租户未找到" ) );

        return ClientMemberProfile.builder( )
                .memberId( memberId )
                .memberName( member.name( ) )
                .avatar( member.avatar( ) )
                .tenantId( tenantId )
                .tenantName( tenant.getName( ) )
                .tenantLogo( tenant.getLogo( ) )
                .subdomainPrefix( tenant.getSubdomainPrefix( ) )
                .subdomainReady( tenant.isSubdomainReady( ) )
                .topAppIds( member.toppedAppIds( ) )
                .hideBottomMryLogo( tenant.getPackages( ).hideBottomMryLogo( ) )
                .reportingAllowed( tenant.getPackages( ).reportingAllowed( ) )
                .kanbanAllowed( tenant.getPackages( ).kanbanAllowed( ) )
                .assignmentAllowed( tenant.getPackages( ).assignmentAllowed( ) )
                .build( );
    }

    public MemberInfo fetchMyMemberInfo( User user ) {
        rateLimiter.applyFor( user.tenantId( ), "Member:FetchMyMemberInfo", 10 );

        String memberId = user.memberId( );
        Member member = memberRepository.findPresent( memberId );
        List< String > departmentIds = member.departmentIds( );
        List< String > departmentNames = List.of( );

        if ( isNotEmpty( departmentIds ) ) {
            DepartmentHierarchy departmentHierarchy = departmentHierarchyRepository.findByTenantId( user.tenantId( ) );
            List< TenantCachedDepartment > cachedDepartments = departmentRepository.tenantCachedDepartments( departmentHierarchy.tenantId( ) );
            Map< String, String > allDepartmentNames = cachedDepartments.stream( )
                    .collect( toImmutableMap( TenantCachedDepartment::id, TenantCachedDepartment::name ) );
            Map< String, String > departmentFullNames = departmentHierarchy.departmentFullNames( allDepartmentNames );
            departmentNames = departmentIds.stream( ).map( departmentFullNames::get ).filter( Objects::nonNull ).collect( toImmutableList( ) );
        }

        return MemberInfo.builder( )
                .memberId( member.identifier( ) )
                .tenantId( member.tenantId( ) )
                .name( member.name( ) )
                .email( member.email( ) )
                .mobile( member.mobile( ) )
                .wxNickName( "member.getWxNickName( )" )
                .wxBound( false )
                .role( member.role( ) )
                .departments( departmentNames )
                .build( );
    }

    public MemberBaseSetting fetchMyBaseSetting( User user ) {
        rateLimiter.applyFor( user.tenantId( ), "Member:FetchMyBaseSetting", 10 );

        String memberId = user.memberId( );
        Member member = memberRepository.findPresent( memberId );
        return MemberBaseSetting.builder( ).id( memberId ).name( member.name( ) ).build( );
    }

    public List< MemberReferenced > listMemberReferences( String tenantId, User user ) {
        rateLimiter.applyFor( user.tenantId( ), "Member:FetchAllMemberReferencesTenant", 100 );

        user.checkIsLoggedInFor( tenantId );
        return doListMemberReferences( user.tenantId( ) );
    }

    public List< MemberReferenced > listMemberReferences( User user ) {
        rateLimiter.applyFor( user.tenantId( ), "Member:FetchAllMemberReferences", 100 );

        return doListMemberReferences( user.tenantId( ) );
    }

    private List< MemberReferenced > doListMemberReferences( String tenantId ) {
        List< MemberReference > memberReferences = memberRepository.tenantCachedMembers( tenantId )
                .stream( )
                .map( toMemberReference( ) )
                .collect( toImmutableList( ) );
        return memberReferences.stream( )
                .sorted( ( o1, o2 ) -> CHINESE_COLLATOR.compare( o1.name( ), o2.name( ) ) )
                .map( member -> {
                    String suffix = isNotBlank( member.mobile( ) ) ? "（" + maskMobile( member.mobile( ) ) + "）" : "";

                    return MemberReferenced.builder( )
                            .id( member.id( ) )
                            .showName( member.name( ) + suffix )
                            .build( );
                } )
                .collect( toImmutableList( ) );
    }

    private Function< TenantCachedMember, MemberReference > toMemberReference( ) {
        return cachedMember -> MemberReference.builder( )
                .id( cachedMember.id( ) )
                .name( cachedMember.name( ) )
                .mobile( cachedMember.mobile( ) )
                .email( cachedMember.email( ) )
                .build( );
    }
}
