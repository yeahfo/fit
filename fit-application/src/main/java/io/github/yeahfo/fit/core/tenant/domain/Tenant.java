package io.github.yeahfo.fit.core.tenant.domain;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.github.yeahfo.fit.core.common.domain.AggregateRoot;
import io.github.yeahfo.fit.core.common.domain.UploadedFile;
import io.github.yeahfo.fit.core.common.domain.user.User;
import io.github.yeahfo.fit.core.common.exception.FitException;
import io.github.yeahfo.fit.core.order.domain.delivery.Consignee;
import io.github.yeahfo.fit.core.plan.domain.Plan;
import io.github.yeahfo.fit.core.plan.domain.PlanType;
import io.github.yeahfo.fit.core.tenant.domain.events.*;
import io.github.yeahfo.fit.core.tenant.domain.invoice.InvoiceTitle;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.*;

import static io.github.yeahfo.fit.core.common.exception.ErrorCode.*;
import static io.github.yeahfo.fit.core.common.utils.MapUtils.mapOf;
import static io.github.yeahfo.fit.core.common.utils.Identified.isDuplicated;
import static java.time.Instant.now;
import static java.time.LocalDate.ofInstant;
import static java.time.ZoneId.systemDefault;
import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.lang3.StringUtils.isNotBlank;


@Slf4j
@Getter
@NoArgsConstructor( access = PRIVATE )
public class Tenant extends AggregateRoot {
    private static final Set< String > FORBIDDEN_SUBDOMAIN_PREFIXES = Set.of( "www", "ww", "help", "helps", "api", "apis", "image", "images",
            "doc", "docs", "blog", "blogs", "admin", "administrator", "ops", "kibana", "console", "consoles", "manager", "managers",
            "mry", "contact", "contacts", "new", "news", "mail", "mails", "ftp", "ftps", "me", "my", "video", "videos", "tv", "sex", "porn", "naked",
            "official", "gov", "government", "file", "files", "hr", "job", "work", "career", "forum", "m", "static" );

    protected String name;//租户名称
    protected Packages packages;//当前套餐
    protected ResourceUsage resourceUsage;//当前资源使用量统计
    protected UploadedFile logo;//Logo
    protected UploadedFile loginBackground;//登录背景图片
    protected String subdomainPrefix;//子域名前缀
    protected boolean subdomainReady;//子域名是否生效
    protected String subdomainRecordId;//阿里云返回的DNS记录ID
    protected Instant subdomainUpdatedAt;//子域名更新时间
    protected ApiSetting apiSetting;//API集成设置
    protected boolean active;//用于后台管理端设置，非active时所有成员无法登录，无法访问API
    protected InvoiceTitle invoiceTitle;//发票抬头
    protected List< Consignee > consignees;//收货人

    private Tenant( String name, User user ) {
        super( user.tenantId( ), user );
        this.name = name;
        this.packages = Packages.init( );
        this.resourceUsage = ResourceUsage.init( );
        this.apiSetting = ApiSetting.init( );
        this.active = true;
        this.consignees = new ArrayList<>( 3 );
        addOpsLog( "注册", user );
    }

    public static ResultWithDomainEvents< Tenant, TenantDomainEvent > create( String name, User user ) {
        return new ResultWithDomainEvents<>( new Tenant( name, user ), new TenantCreatedEvent( user ) );
    }

    public ResultWithDomainEvents< Tenant, TenantDomainEvent > updatePlanType( PlanType planType, Instant expireAt, User user ) {
        this.packages.updatePlanType( planType, expireAt );
        addOpsLog( "设置套餐为" + planType.getName( ) + "(" + ofInstant( expireAt, systemDefault( ) ) + "过期)", user );
        return new ResultWithDomainEvents<>( this, new TenantPlanUpdatedEvent( planType, user ) );
    }

    public ResultWithDomainEvents< Tenant, TenantDomainEvent > updateMemberCount( long memberCount, User user ) {
        this.resourceUsage.updateMemberCount( memberCount );
        return new ResultWithDomainEvents<>( this, new TenantResourceUsageUpdatedEvent( user ) );
    }

    public void useSms( ) {
        this.resourceUsage.increaseSmsSentCountForCurrentMonth( );

        if ( this.resourceUsage.getSmsSentCountForCurrentMonth( ) > this.packages.effectiveMaxSmsCountPerMonth( ) ) {
            this.packages.tryUseExtraRemainSms( );
        }
    }

    public ResultWithDomainEvents< Tenant, TenantDomainEvent > updateBaseSetting( String name, UploadedFile loginBackground, User user ) {
        this.name = name;
        this.loginBackground = loginBackground;
        addOpsLog( "更新基本设置", user );
        return new ResultWithDomainEvents<>( this, new TenantBaseSettingUpdatedEvent( user ) );
    }

    public PackagesStatus packagesStatus( ) {
        return PackagesStatus.builder( ).id( this.identifier( ) ).packages( this.packages ).resourceUsage( this.resourceUsage ).build( );
    }

    public void updateLogo( UploadedFile logo, User user ) {
        if ( Objects.equals( this.logo, logo ) ) {
            return;
        }
        this.logo = logo;
        addOpsLog( "更新Logo", user );
    }

    public ResultWithDomainEvents< Tenant, TenantDomainEvent > updateSubdomain( String newSubdomainPrefix, User user ) {
        if ( isNotBlank( newSubdomainPrefix ) && FORBIDDEN_SUBDOMAIN_PREFIXES.contains( newSubdomainPrefix ) ) {
            throw new FitException( FORBIDDEN_SUBDOMAIN_PREFIX, "不允许使用该子域名。", mapOf( "subdomainPrefix", newSubdomainPrefix ) );
        }
        List< TenantDomainEvent > events = new ArrayList<>( );
        String oldSubdomainPrefix = this.subdomainPrefix;
        if ( !Objects.equals( oldSubdomainPrefix, newSubdomainPrefix ) ) {
            if ( !subdomainUpdatable( ) ) {
                throw new FitException( SUBDOMAIN_UPDATED_TOO_OFTEN,
                        "子域名30天内之内只能更新一次。", mapOf( "subdomainPrefix", newSubdomainPrefix ) );
            }

            events.add( new TenantSubdomainUpdatedEvent( oldSubdomainPrefix, newSubdomainPrefix, user ) );
            this.subdomainPrefix = newSubdomainPrefix;
            this.subdomainReady = false;
            this.subdomainUpdatedAt = now( );
            addOpsLog( "更新域名前缀为" + newSubdomainPrefix, user );
        }
        return new ResultWithDomainEvents<>( this, events );
    }

    public boolean subdomainUpdatable( ) {
        return subdomainUpdatedAt == null || now( ).minusSeconds( 30 * 24 * 3600L ).isAfter( subdomainUpdatedAt );
    }

    public void refreshApiSecret( User user ) {
        this.apiSetting.refreshApiSecret( );
        addOpsLog( "刷新API Secret", user );
    }

    public ResultWithDomainEvents< Tenant, TenantDomainEvent > updateInvoiceTitle( InvoiceTitle title, User user ) {
        if ( Objects.equals( title, this.invoiceTitle ) ) {
            return new ResultWithDomainEvents<>( this );
        }
        this.invoiceTitle = title;
        addOpsLog( "更新发票抬头", user );
        return new ResultWithDomainEvents<>( this, new TenantInvoiceTitleUpdatedEvent( user ) );
    }

    public void addConsignee( Consignee consignee, User user ) {
        if ( this.consignees.size( ) >= 5 ) {
            throw new FitException( MAX_CONSIGNEE_REACHED, "最多只能添加5个收货人信息。", mapOf( "tenantId", this.identifier( ) ) );
        }

        this.consignees.addFirst( consignee );

        if ( isDuplicated( this.consignees ) ) {
            throw new FitException( CONSIGNEE_ID_DUPLICATED, "收货人ID重复。", mapOf( "tenantId", this.identifier( ) ) );
        }

        addOpsLog( "添加收货人(" + consignee.name( ) + ")", user );
    }

    public void updateConsignee( Consignee newConsignee, User user ) {
        this.consignees = this.consignees.stream( )
                .map( consignee -> Objects.equals( consignee.identifier( ), newConsignee.identifier( ) ) ? newConsignee : consignee )
                .collect( toList( ) );
        addOpsLog( "更新收货人(" + newConsignee.name( ) + ")", user );
    }

    public void deleteConsignee( String consigneeId, User user ) {
        Optional< Consignee > consigneeOptional = this.consignees.stream( )
                .filter( consignee -> Objects.equals( consignee.identifier( ), consigneeId ) ).findFirst( );

        if ( consigneeOptional.isEmpty( ) ) {
            log.warn( "No consignee[{}] found to delete, skip.", consigneeId );
            return;
        }

        this.consignees.removeIf( consignee -> Objects.equals( consignee.identifier( ), consigneeId ) );
        addOpsLog( "删除收货人(" + consigneeOptional.get( ).name( ) + ")", user );
    }

    public Plan currentPlan( ) {
        return this.packages.currentPlan( );
    }

    public PlanType currentPlanType( ) {
        return this.packages.currentPlanType( );
    }

    public boolean isPackagesExpired( ) {
        return this.packages.isExpired( );
    }

    public Instant packagesExpiredAt( ) {
        return this.packages.expireAt( );
    }
}
