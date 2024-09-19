package io.github.yeahfo.fit.core.tenant.domain;

import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.github.yeahfo.fit.core.common.domain.AggregateRoot;
import io.github.yeahfo.fit.core.common.domain.UploadedFile;
import io.github.yeahfo.fit.core.common.domain.user.User;
import io.github.yeahfo.fit.core.order.domain.delivery.Consignee;
import io.github.yeahfo.fit.core.plan.domain.PlanType;
import io.github.yeahfo.fit.core.tenant.domain.events.TenantCreatedEvent;
import io.github.yeahfo.fit.core.tenant.domain.events.TenantDomainEvent;
import io.github.yeahfo.fit.core.tenant.domain.events.TenantPlanUpdatedEvent;
import io.github.yeahfo.fit.core.tenant.domain.events.TenantResourceUsageUpdatedEvent;
import io.github.yeahfo.fit.core.tenant.domain.invoice.InvoiceTitle;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.time.LocalDate.ofInstant;
import static java.time.ZoneId.systemDefault;
import static lombok.AccessLevel.PRIVATE;


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
}
