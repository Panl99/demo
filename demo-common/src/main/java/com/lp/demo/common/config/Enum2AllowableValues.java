package com.lp.demo.common.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 动态添加swagger枚举文档
 * 代替参数allowableValues
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Enum2AllowableValues {

    Class<? extends Enum<?>> value();

    String method() default "name"; // 默认使用 name 方法
}