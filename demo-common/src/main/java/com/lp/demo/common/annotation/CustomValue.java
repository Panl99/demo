package com.lp.demo.common.annotation;

import com.lp.demo.common.validator.CustomValueValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lp
 * @date 2024/1/19 15:44
 * @desc 枚举值：用于校验实体类中某个属性的值必须为指定的值，如1或者2
 **/
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {CustomValueValidator.class})
public @interface CustomValue {

    /**
     * 错误消息
     */
    String message() default "必须为指定值";

    /**
     * 字符串值
     */
    String[] strValues() default {};

    /**
     * 数字值
     */
    int[] intValues() default {};

    /**
     * 分组
     */
    Class<?>[] groups() default {};

    /**
     * 负载
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * 指定多个时使用
     */
    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        CustomValue[] value();
    }
}
