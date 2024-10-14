package io.github.yeahfo.fit.core.tenant.domain;

import lombok.*;

import static io.github.yeahfo.fit.core.common.utils.UuidGenerator.newShortUuid;
import static lombok.AccessLevel.PRIVATE;

@Getter
@Builder
@EqualsAndHashCode
@NoArgsConstructor( access = PRIVATE )
@AllArgsConstructor( access = PRIVATE )
public class ApiSetting {
    private String apiKey;
    private String apiSecret;

    public static ApiSetting init( ) {
        return ApiSetting.builder( ).apiKey( newShortUuid( ) ).apiSecret( newShortUuid( ) ).build( );
    }

    public void refreshApiSecret( ) {
        this.apiSecret = newShortUuid( );
    }
}
