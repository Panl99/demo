package com.lp.demo.common.aop.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SaveLog {

    // 需要保证和params 长度一致，并且对应上
    int[] paramsIdxes() default {};

    // 参数名称列表
    String[] params() default {};

    // 场景
    String scene() default "";

    // 掩码定义
    MaskLog[] masks() default {};

    @AliasFor("params")
    String[] value() default {};

    /**
     * 参数位置对应对象的操作内容展示字段
     */
    String[] actionFields() default {};

}
