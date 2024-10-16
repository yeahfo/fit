package io.github.yeahfo.fit.common.utils;

import java.text.Collator;
import java.time.format.DateTimeFormatter;

import static java.time.ZoneId.systemDefault;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Locale.CHINA;

public interface Constants {
    interface Profiles {
        String prod = "prod";
        String production = "production";
        String dev = "dev";
        String development = "development";
    }

    String CHINA_TIME_ZONE = "Asia/Shanghai";
    String AUTH_COOKIE_NAME = "fittoken";
    String NO_TENANT_ID = "NO_TENANT_ID";
    DateTimeFormatter FIT_DATE_TIME_FORMATTER = ofPattern( "yyyy-MM-dd HH:mm" ).withZone( systemDefault( ) );
    DateTimeFormatter FIT_DATE_FORMATTER = ofPattern( "yyyy-MM-dd" ).withZone( systemDefault( ) );
    Collator CHINESE_COLLATOR = Collator.getInstance( CHINA );
    int MAX_PER_PAGE_CONTROL_SIZE = 20;
    int MAX_APP_MANAGER_SIZE = 10;
    int MAX_PER_APP_PAGE_SIZE = 20;
    int MAX_PER_APP_ATTRIBUTE_SIZE = 20;
    int MAX_PER_APP_OPERATION_MENU_SIZE = 20;
    int MAX_PER_APP_NUMBER_REPORT_SIZE = 20;
    int MAX_PER_CHART_REPORT_SIZE = 20;
    int MAX_GROUP_MANAGER_SIZE = 10;
    int MAX_GROUP_HIERARCHY_LEVEL = 5;
    int MIN_SUBDOMAIN_LENGTH = 2;
    int MAX_SUBDOMAIN_LENGTH = 20;
    int MAX_CUSTOM_ID_LENGTH = 50;
    int MAX_GENERIC_NAME_LENGTH = 50;
    int MAX_SHORT_NAME_LENGTH = 10;
    int MAX_PLACEHOLDER_LENGTH = 50;
    int MAX_DIRECT_ATTRIBUTE_VALUE_LENGTH = 100;
    int MAX_URL_LENGTH = 1024;
    int MAX_PARAGRAPH_LENGTH = 50000;
    int MIN_MARGIN = 0;
    int MAX_MARGIN = 100;
    int MIN_PADDING = 0;
    int MAX_PADDING = 100;
    int MIN_BORDER_RADIUS = 0;
    int MAX_BORDER_RADIUS = 100;
    String EVENT_COLLECTION = "event";
    String DEPARTMENT_COLLECTION = "department";
    String DEPARTMENT_HIERARCHY_COLLECTION = "department_hierarchy";
    String GROUP_COLLECTION = "group";
    String GROUP_HIERARCHY_COLLECTION = "group_hierarchy";
    String APP_COLLECTION = "app";
    String APP_MANUAL_COLLECTION = "app_manual";
    String ASSIGNMENT_PLAN_COLLECTION = "assignment_plan";
    String ASSIGNMENT_COLLECTION = "assignment";
    String ORDER_COLLECTION = "order";
    String MEMBER_COLLECTION = "member";
    String QR_COLLECTION = "qr";
    String PLATE_BATCH_COLLECTION = "plate_batch";
    String PLATE_COLLECTION = "plate";
    String SUBMISSION_COLLECTION = "submission";
    String TENANT_COLLECTION = "tenant";
    String VERIFICATION_COLLECTION = "verification";
    String PLATE_TEMPLATE_COLLECTION = "plate_template";
    String SHEDLOCK_COLLECTION = "shedlock";
    String APP_CACHE = "APP";
    String TENANT_APPS_CACHE = "TENANT_APPS";
    String GROUP_CACHE = "GROUP";
    String APP_GROUPS_CACHE = "APP_GROUPS";
    String GROUP_HIERARCHY_CACHE = "GROUP_HIERARCHY";
    String MEMBER_CACHE = "MEMBER";
    String TENANT_MEMBERS_CACHE = "TENANT_MEMBERS";
    String TENANT_DEPARTMENTS_CACHE = "TENANT_DEPARTMENTS";
    String DEPARTMENT_HIERARCHY_CACHE = "DEPARTMENT_HIERARCHY";
    String TENANT_CACHE = "TENANT";
    String API_TENANT_CACHE = "API_TENANT";
    String OPEN_ASSIGNMENT_PAGES_CACHE = "OPEN_ASSIGNMENT_PAGES";
    String AUTHORIZATION = "Authorization";
    String BEARER = "Bearer ";
    String ALL = "ALL";
    String REDIS_DOMAIN_EVENT_CONSUMER_GROUP = "domain.event.group";
    String REDIS_WEBHOOK_CONSUMER_GROUP = "webhook.group";
    String REDIS_NOTIFICATION_CONSUMER_GROUP = "notification.group";
}
