package com.lp.demo.action.spring_in_action.debounce.annotation;


import com.lp.demo.action.spring_in_action.debounce.keyparser.DebounceKeyParser;
import com.lp.demo.action.spring_in_action.debounce.keyparser.DefaultDebounceKeyParser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;


/**
 * @author lp
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Debounce {

    /**
     * 超时时间，默认为 1 秒
     * 如果任务执行时间超过它，请求还是会进来
     */
    int timeout() default 1000;

    /**
     * 时间单位，默认为毫秒
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    /**
     * 提示信息
     */
    String message() default "请求繁忙，请稍后重试!";

    /**
     * 使用的 Key 解析器
     */
    Class<? extends DebounceKeyParser> keyParser() default DefaultDebounceKeyParser.class;

    /**
     * 使用的 Key 参数
     */
    String keyArg() default "";

}
