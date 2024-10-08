package io.github.yeahfo.fit.core.common.validation.mobileoremail;

import io.github.yeahfo.fit.core.common.utils.FitRegexConstants;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class MobileOrEmailValidator implements ConstraintValidator< MobileOrEmail, String > {
    private static final Pattern MOBILE_PATTERN = Pattern.compile( FitRegexConstants.MOBILE_PATTERN );
    private static final Pattern EMAIL_PATTERN = Pattern.compile( FitRegexConstants.EMAIL_PATTERN, CASE_INSENSITIVE );

    @Override
    public void initialize( MobileOrEmail constraintAnnotation ) {
    }

    @Override
    public boolean isValid( String value, ConstraintValidatorContext context ) {
        if ( isBlank( value ) ) {
            return true;
        }

        return value.length( ) <= 50 && ( MOBILE_PATTERN.matcher( value ).matches( ) || EMAIL_PATTERN.matcher( value ).matches( ) );
    }
}
