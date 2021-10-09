package com.lp.demo.iot.annotation;


import com.lp.demo.iot.enums.BusinessTypeEnum;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lp
 * @date 2021/5/31 13:54
 **/
@Documented
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BusinessType {
    BusinessTypeEnum value();
}