package io.github.yeahfo.fit.core.tenant.domain;

import io.github.yeahfo.fit.core.common.exception.FitException;
import lombok.Builder;

import static io.github.yeahfo.fit.core.common.exception.ErrorCode.*;
import static io.github.yeahfo.fit.core.common.utils.MapUtils.mapOf;
import static io.github.yeahfo.fit.management.FitManageTenant.ADMIN_MEMBER_ID;
import static io.github.yeahfo.fit.management.FitManageTenant.FIT_MANAGE_TENANT_ID;

@Builder
public record PackagesStatus(
        String id,//租户ID
        Packages packages,//租户当前套餐
        ResourceUsage resourceUsage//租户当前的资源使用量
) {
    public static final int MAX_PLATE_SIZE = 100000000;

    public boolean isMaxMemberReached( ) {
        long maxAllowedMemberCount = packages.effectiveMaxMemberCount( );
        long currentMemberCount = resourceUsage.getMemberCount( );
        return currentMemberCount >= maxAllowedMemberCount;
    }

    public boolean isMaxAppReached( ) {
        if ( FIT_MANAGE_TENANT_ID.equals( id ) || ADMIN_MEMBER_ID.equals( id ) ) {
            return false;
        }

        long maxAllowedAppCount = packages.effectiveMaxAppCount( );
        long currentAppCount = resourceUsage.getAppCount( );
        return currentAppCount >= maxAllowedAppCount;
    }


    public boolean isMaxSmsCountReached( ) {
        if ( resourceUsage.getSmsSentCountForCurrentMonth( ) < packages.effectiveMaxSmsCountPerMonth( ) ) {
            return false;
        }

        return packages.getExtraRemainSmsCount( ) <= 0;
    }

    public void validateAddMember( ) {
        if ( isMaxMemberReached( ) ) {
            if ( isExpired( ) ) {
                throw new FitException( MEMBER_COUNT_LIMIT_REACHED,
                        "当前套餐(" + currentPlanName( ) + ")已过期，且成员总数已达免费版上限，无法继续添加成员，如需添加请及时续费或升级。",
                        mapOf( "tenantId", tenantId( ) ) );
            }
            throw new FitException( MEMBER_COUNT_LIMIT_REACHED,
                    "无法继续添加成员，成员总数已经达到当前套餐(" + currentPlanName( ) + ")上限，如需添加请及时升级或增购成员数量。",
                    mapOf( "tenantId", tenantId( ) ) );
        }
    }

    private String tenantId( ) {
        return id;
    }

    private boolean isExpired( ) {
        return packages.isExpired( );
    }

    private String currentPlanName( ) {
        return packages.currentPlanName( );
    }

    public void validateImportMember( ) {
        if ( !this.packages.batchImportMemberAllowed( ) ) {
            if ( isExpired( ) ) {
                throw new FitException( BATCH_MEMBER_IMPORT_NOT_ALLOWED,
                        "当前套餐(" + currentPlanName( ) + ")已过期，有效套餐已降为免费版，无法使用批量导入功能，请及时续费或升级。",
                        mapOf( "tenantId", tenantId( ) ) );
            }
            throw new FitException( BATCH_MEMBER_IMPORT_NOT_ALLOWED,
                    "当前套餐(" + currentPlanName( ) + ")无法使用批量导入功能，请及时升级。",
                    mapOf( "tenantId", tenantId( ) ) );
        }

    }

    public long validateImportMembers( ) {
        long maxAllowedMemberCount = packages.effectiveMaxMemberCount( );
        long currentMemberCount = resourceUsage.getMemberCount( );

        if ( currentMemberCount >= maxAllowedMemberCount ) {
            if ( isExpired( ) ) {
                throw new FitException( MEMBER_COUNT_LIMIT_REACHED,
                        "上传失败，当前套餐(" + currentPlanName( ) + ")已过期，有效套餐已降为免费版，且当前成员总数已达免费版上限(" + maxAllowedMemberCount + ")，无法继续上传，如需继续请及时续费或升级。",
                        mapOf( "tenantId", tenantId( ) ) );
            }
            throw new FitException( MEMBER_COUNT_LIMIT_REACHED,
                    "上传失败，成员总数已达当前套餐(" + currentPlanName( ) + ")上限(" + maxAllowedMemberCount + ")，如需继续请及时升级。",
                    mapOf( "tenantId", tenantId( ) ) );
        }
        return maxAllowedMemberCount - currentMemberCount;//返回可上传数量
    }

    public void validateUpdateSubdomain( ) {
        if ( !packages.customSubdomainAllowed( ) ) {
            if ( isExpired( ) ) {
                throw new FitException( UPDATE_SUBDOMAIN_NOT_ALLOWED,
                        "当前套餐(" + currentPlanName( ) + ")已过期，有效套餐已降为免费版，无法设置子域名，如需设置请及时续费或升级。",
                        mapOf( "tenantId", tenantId( ) ) );
            }
            throw new FitException( UPDATE_SUBDOMAIN_NOT_ALLOWED,
                    "当前套餐(" + currentPlanName( ) + ")无法设置子域名，如需设置请及时升级。",
                    mapOf( "tenantId", tenantId( ) ) );
        }
    }

    public void validateUpdateLogo( ) {
        if ( !packages.customLogoAllowed( ) ) {
            if ( isExpired( ) ) {
                throw new FitException( UPDATE_LOGO_NOT_ALLOWED,
                        "当前套餐(" + currentPlanName( ) + ")已过期，有效套餐已降为免费版，无法设置Logo，如需设置请及时续费或升级。",
                        mapOf( "tenantId", tenantId( ) ) );
            }
            throw new FitException( UPDATE_LOGO_NOT_ALLOWED,
                    "当前套餐(" + currentPlanName( ) + ")无法设置Logo，如需设置请及时升级。",
                    mapOf( "tenantId", tenantId( ) ) );
        }
    }

    public void validateRefreshApiSecret( ) {
        if ( !packages.developerAllowed( ) ) {
            if ( isExpired( ) ) {
                throw new FitException( REFRESH_API_SECRET_NOT_ALLOWED,
                        "当前套餐(" + currentPlanName( ) + ")已过期，有效套餐已降为免费版，无法刷新API Secret，如需刷新请及时续费或升级。",
                        mapOf( "tenantId", tenantId( ) ) );
            }
            throw new FitException( REFRESH_API_SECRET_NOT_ALLOWED,
                    "当前套餐(" + currentPlanName( ) + ")无法刷新API Secret，如需刷新请及时升级。",
                    mapOf( "tenantId", tenantId( ) ) );
        }
    }

    public void validateAddApp( ) {
        if ( isMaxAppReached( ) ) {
            if ( isExpired( ) ) {
                throw new FitException( APP_COUNT_LIMIT_REACHED,
                        "当前套餐(" + currentPlanName( ) + ")已过期，有效套餐已降为免费版，无法新建应用，如需继续请及时续费或升级。",
                        mapOf( "tenantId", tenantId( ) ) );
            }
            throw new FitException( APP_COUNT_LIMIT_REACHED,
                    "新建应用失败，应用总数已经达到当前套餐(" +
                            currentPlanName( ) + ")的上限(" + packages.effectiveMaxAppCount( ) + "个)，如需继续请及时升级。",
                    mapOf( "tenantId", tenantId( ) ) );
        }
    }
}
