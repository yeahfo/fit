package io.github.yeahfo.fit.core.common.validation.verficationcode;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

import static io.github.yeahfo.fit.core.common.utils.FitRegexConstants.VERIFICATION_CODE_PATTERN;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class VerificationCodeValidator implements ConstraintValidator< VerificationCode, String > {

    private static final Pattern PATTERN = Pattern.compile( VERIFICATION_CODE_PATTERN );

    @Override
    public void initialize( VerificationCode constraintAnnotation ) {
    }

    @Override
    public boolean isValid( String value, ConstraintValidatorContext context ) {
        if ( isBlank( value ) ) {
            return true;
        }

        return PATTERN.matcher( value ).matches( );
    }
}
