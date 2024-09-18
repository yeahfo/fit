package io.github.yeahfo.fit.core.tenant.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.time.Instant.now;
import static java.time.ZoneId.systemDefault;
import static java.time.format.DateTimeFormatter.ofPattern;
import static lombok.AccessLevel.PRIVATE;

@EqualsAndHashCode
@Builder( access = PRIVATE )
@AllArgsConstructor( access = PRIVATE )
public class ResourceUsage {
    private static class SmsSentCount {
        private static final DateTimeFormatter MONTH_FORMATTER = ofPattern( "yyyy-MM" ).withZone( systemDefault( ) );
        private String month;
        private int count;

        private int getSmsSentCountForCurrentMonth( ) {
            return isAtCurrentMonth( ) ? count : 0;
        }

        private void increaseSentCountForCurrentMonth( ) {
            if ( isAtCurrentMonth( ) ) {
                count++;
            } else {
                this.month = currentMonth( );
                this.count = 1;
            }
        }

        private boolean isAtCurrentMonth( ) {
            return Objects.equals( month, currentMonth( ) );
        }

        private String currentMonth( ) {
            return MONTH_FORMATTER.format( now( ) );
        }
    }

    private long appCount;//已创建应用总数
    private long memberCount;//已创建成员总数
    private long departmentCount;//已创建的部门数
    private float storage;//已使用的存储占用量(GB)
    private long plateCount;//已创建码牌总数
    private SmsSentCount smsSentCount;//本月短信发送量

    private Map< String, Integer > qrCountPerApp;//每个应用对应的QR数量，appId -> qr count
    private Map< String, Integer > groupCountPerApp;//每个应用对应的group数量，appId -> group count
    private Map< String, Integer > submissionCountPerApp;//每个应用的提交数量，appId -> submission count

    public static ResourceUsage init( ) {
        return ResourceUsage.builder( )
                .appCount( 0 )
                .memberCount( 0 )
                .departmentCount( 0 )
                .storage( 0 )
                .plateCount( 0 )
                .qrCountPerApp( new HashMap<>( ) )
                .groupCountPerApp( new HashMap<>( ) )
                .submissionCountPerApp( new HashMap<>( ) )
                .smsSentCount( new SmsSentCount( ) )
                .build( );
    }

    public void updateMemberCount( long memberCount ) {
        this.memberCount = memberCount;
    }

}
