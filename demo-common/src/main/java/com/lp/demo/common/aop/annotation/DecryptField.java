package com.lp.demo.common.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 解密字段注解
 */
@Target(value = {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DecryptField {

    /**
     * 是否掩码
     */
    boolean isMask() default false;

    /**
     * 掩码程度
     */
    MaskLog.MaskLevelEnum maskLevel() default MaskLog.MaskLevelEnum.PART;

}