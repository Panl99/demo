package com.lp.demo.common.annotation.constraints;

import com.lp.demo.common.validator.IdCardValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 身份证格式校验
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {IdCardValidator.class})
public @interface IdCard {

    String message() default "证件号格式错误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
