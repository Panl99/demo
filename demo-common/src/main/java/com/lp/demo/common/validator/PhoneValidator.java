package com.lp.demo.common.validator;

import com.lp.demo.common.annotation.constraints.Phone;
import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

import static com.lp.demo.common.constant.PatternRegexp.PHONE;

public class PhoneValidator implements ConstraintValidator<Phone, String> {

    @Override
    public void initialize(Phone constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        //如果为空则不校验
        return StringUtils.isBlank(value) || Pattern.matches(PHONE, value);
    }
}
