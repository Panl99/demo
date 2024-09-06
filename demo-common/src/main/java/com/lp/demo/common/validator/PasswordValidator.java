package com.lp.demo.common.validator;

import com.lp.demo.common.annotation.constraints.Password;
import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

import static com.lp.demo.common.constant.PatternRegexp.PASSWORD;


public class PasswordValidator implements ConstraintValidator<Password, String> {

    private String regexp;

    @Override
    public void initialize(Password constraintAnnotation) {
        regexp = StringUtils.isBlank(constraintAnnotation.regexp()) ? PASSWORD : constraintAnnotation.regexp();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        //如果为空则不校验
        return StringUtils.isBlank(value) || Pattern.matches(regexp, value);
    }
}
