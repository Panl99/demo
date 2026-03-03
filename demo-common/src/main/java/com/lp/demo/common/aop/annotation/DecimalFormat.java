package com.lp.demo.common.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.RoundingMode;

/**
 * @author lp
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DecimalFormat {
    /**
     * 小数位数，-1表示不设置
     */
    int scale() default -1;

    /**
     * 是否去除末尾零
     */
    boolean stripTrailingZeros() default true;

    /**
     * 精度
     */
    RoundingMode roundingMode() default RoundingMode.HALF_UP;
}