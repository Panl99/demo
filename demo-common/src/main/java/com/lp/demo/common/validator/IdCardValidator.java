package com.lp.demo.common.validator;

import cn.hutool.core.util.IdcardUtil;
import com.lp.demo.common.annotation.constraints.IdCard;
import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IdCardValidator implements ConstraintValidator<IdCard, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return StringUtils.isBlank(value) || IdcardUtil.isValidCard(value);
    }
}
