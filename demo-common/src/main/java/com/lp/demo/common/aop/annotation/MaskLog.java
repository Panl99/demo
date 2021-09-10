package com.lp.demo.common.aop.annotation;

/**
 *  用于实现日志记录注解中字段掩码实现
 *  比如隐藏密码打印
 */
public @interface MaskLog {
    // 需要掩码的参数位置
    int paramsIdx() default 0;

    // 参数位置对应对象的字段， 如果字段为空，此时整个参数以掩码形式打印，否则，对应的字段以掩码形式展现
    String[] fields() default {};
}
