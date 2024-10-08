package io.github.yeahfo.fit.core.tenant.application;

import io.github.yeahfo.fit.core.common.application.Command;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;

import static io.github.yeahfo.fit.core.common.utils.FitConstants.MAX_SUBDOMAIN_LENGTH;
import static io.github.yeahfo.fit.core.common.utils.FitConstants.MIN_SUBDOMAIN_LENGTH;
import static io.github.yeahfo.fit.core.common.utils.FitRegexConstants.SUBDOMAIN_PATTERN;

@Builder
public record UpdateTenantSubdomainCommand( @Size( min = MIN_SUBDOMAIN_LENGTH, max = MAX_SUBDOMAIN_LENGTH )
                                            @Pattern( regexp = SUBDOMAIN_PATTERN, message = "子域名格式不正确" )
                                            String subdomainPrefix ) implements Command {
    @Override
    public void correctAndValidate( ) {
        if ( StringUtils.contains( subdomainPrefix, " " ) ) {
            throw new ValidationException( "不得包含空格" );
        }
    }
}
