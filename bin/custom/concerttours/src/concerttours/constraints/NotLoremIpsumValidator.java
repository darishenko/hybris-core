package concerttours.constraints;

import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotLoremIpsumValidator implements ConstraintValidator<NotLoremIpsum, String> {
    private static final String LOREM_IPSUM = "lorem ipsum";

    @Override
    public void initialize(final NotLoremIpsum constraintAnnotation) {
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        return StringUtils.isBlank(value) && !value.toLowerCase().startsWith(LOREM_IPSUM);
    }
}