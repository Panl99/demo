package com.lp.demo.common.validator;

import com.lp.demo.common.annotation.CustomValue;
import lombok.Data;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;


public class CustomValueValidator implements ConstraintValidator<CustomValue, Object> {

    private String[] strValues;
    private int[] intValues;

    @Override
    public void initialize(CustomValue constraintAnnotation) {
        strValues = constraintAnnotation.strValues();
        intValues = constraintAnnotation.intValues();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        if (value instanceof String) {
            return Arrays.asList(strValues).contains(value);
        }

        if (value instanceof Integer) {
            return Arrays.asList(intValues).contains(value);
        }

        return false;
    }

    @Data
    static class CustomValueValidTest {
        @CustomValue(intValues = {1, 2, 3})
        private Integer i;
        @CustomValue(strValues = {"aa", "bb", "cc"})
        private String s;
        // 未测试
    }
}
