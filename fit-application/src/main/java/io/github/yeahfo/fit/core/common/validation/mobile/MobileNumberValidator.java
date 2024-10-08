package io.github.yeahfo.fit.core.common.validation.mobile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

import static io.github.yeahfo.fit.core.common.utils.FitRegexConstants.MOBILE_PATTERN;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class MobileNumberValidator implements ConstraintValidator<Mobile, String> {

    private static final Pattern PATTERN = Pattern.compile(MOBILE_PATTERN);

    @Override
    public void initialize(Mobile constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (isBlank(value)) {
            return true;
        }

        return PATTERN.matcher(value).matches();
    }
}
