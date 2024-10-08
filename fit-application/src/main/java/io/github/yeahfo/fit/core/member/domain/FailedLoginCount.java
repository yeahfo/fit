package io.github.yeahfo.fit.core.member.domain;

import lombok.*;

import java.time.LocalDate;

import static java.time.LocalDate.now;
import static lombok.AccessLevel.PRIVATE;

@Getter
@Builder
@EqualsAndHashCode
@NoArgsConstructor( access = PRIVATE )
@AllArgsConstructor( access = PRIVATE )
public class FailedLoginCount {
    private static final int MAX_ALLOWED_FAILED_LOGIN_PER_DAY = 30;

    private LocalDate date;
    private int count;

    public static FailedLoginCount init( ) {
        return FailedLoginCount.builder( ).date( now( ) ).count( 0 ).build( );
    }

    protected void recordFailedLogin( ) {
        LocalDate now = now( );
        if ( now.equals( date ) ) {
            count++;
        } else {
            this.date = now;
            this.count = 0;
        }
    }

    public boolean isLocked( ) {
        return now( ).equals( date ) && this.count >= MAX_ALLOWED_FAILED_LOGIN_PER_DAY;
    }
}
