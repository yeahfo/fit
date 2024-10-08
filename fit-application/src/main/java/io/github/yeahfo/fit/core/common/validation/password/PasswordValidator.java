package io.github.yeahfo.fit.core.common.validation.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

import static io.github.yeahfo.fit.core.common.utils.FitRegexConstants.PASSWORD_PATTERN;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class PasswordValidator implements ConstraintValidator< Password, String > {

    private static final Pattern PATTERN = Pattern.compile( PASSWORD_PATTERN );

    @Override
    public void initialize( Password constraintAnnotation ) {
    }

    @Override
    public boolean isValid( String value, ConstraintValidatorContext context ) {
        if ( isBlank( value ) ) {
            return true;
        }

        return PATTERN.matcher( value ).matches( );
    }
}
