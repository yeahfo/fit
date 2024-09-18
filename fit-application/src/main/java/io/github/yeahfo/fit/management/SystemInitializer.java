package io.github.yeahfo.fit.management;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SystemInitializer implements ApplicationListener< ApplicationReadyEvent > {
    private final FitManageTenant manageTenant;

    @Override
    public void onApplicationEvent( @NonNull ApplicationReadyEvent event ) {
        ensureManageAppsExist( );
    }

    private void ensureManageAppsExist( ) {
        //管理
        manageTenant.init( );
//        tenantManageApp.init( );
//        orderManageApp.init( );
//        printingProductApp.init( );
//        operationApp.init( );
//        offenceReportApp.init( );
//
//        //应用模板
//        appTemplateTenant.init( );
//        appTemplateManageApp.init( );
    }
}
