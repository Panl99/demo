package com.lp.demo.common.util.hutool;

import cn.hutool.core.lang.Assert;
import com.lp.demo.common.exception.DisplayableException;

import java.util.Collections;
import java.util.List;

/**
 * @author lp
 * @date 2022/12/1 9:44
 * @desc
 **/
public class AssertDemo {
    public static void main(String[] args) {
        assertTest();
    }

    public static void assertTest() {
        /**
         * isTrue 是否True
         * isNull 是否是null值，不为null抛出异常
         * notNull 是否非null值
         * notEmpty 是否非空
         * notBlank 是否非空白符
         * notContain 是否为子串
         * notEmpty 是否非空
         * noNullElements 数组中是否包含null元素
         * isInstanceOf 是否类实例
         * isAssignable 是子类和父类关系
         * state 会抛出IllegalStateException异常
         */

        /**
         * 抛 自定义 异常
         */

        try {
            // 表达式为false抛异常
            Assert.isTrue(false, () -> new DisplayableException("Assert.isTrue!"));
        } catch (RuntimeException e) {
            System.out.println("e = " + e);
        }

        try {
            // 表达式为true抛异常
            Assert.isFalse(true, () -> new DisplayableException("Assert.isFalse!"));
        } catch (RuntimeException e) {
            System.out.println("e = " + e);
        }

        try {
            // 参数不为null抛异常
            Assert.isNull("", () -> new DisplayableException("Assert.isNull"));
        } catch (RuntimeException e) {
            System.out.println("e = " + e);
        }

        try {
            // 参数为null抛异常
            Assert.notNull(null, () -> new DisplayableException("Assert.notNull"));
        } catch (RuntimeException e) {
            System.out.println("e = " + e);
        }
        try {
            // 参数为null抛异常，不为null返回对象
            String s = Assert.notNull("111", () -> new DisplayableException("Assert.notNull(return object)"));
            System.out.println("s = " + s);
        } catch (RuntimeException e) {
            System.out.println("e = " + e);
        }

        try {
            // 参数为空抛异常
            List<String> list = Assert.notEmpty(Collections.singletonList("222"), () -> new DisplayableException("Assert.notEmpty(return object)"));
            System.out.println("list = " + list);
        } catch (RuntimeException e) {
            System.out.println("e = " + e);
        }
        try {
            // 参数为空抛异常
            List<String> emptyList = Assert.notEmpty(Collections.emptyList(), () -> new DisplayableException("Assert.notEmpty"));
            System.out.println("emptyList = " + emptyList);
        } catch (RuntimeException e) {
            System.out.println("e = " + e);
        }


        /**
         * 抛java异常
         */

        try {
            // 表达式为false抛非法状态异常
            Assert.state(false, () -> "Assert.state");
        } catch (IllegalStateException e) {
            System.out.println("e = " + e);
        }

        try {
            DisplayableException instanceOf = Assert.isInstanceOf(DisplayableException.class, new DisplayableException("Assert.isInstanceOf"));
            System.out.println("instanceOf = " + instanceOf);
        } catch (IllegalArgumentException e) {
            System.out.println("e = " + e);
        }

        try {
            int index = Assert.checkIndex(5, "Assert.checkIndex".length());
            System.out.println("index = " + index);
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            System.out.println("e = " + e);
        }

        try {
            int between = Assert.checkBetween(5, 0, "Assert.checkBetween".length());
            System.out.println("between = " + between);
        } catch (IllegalArgumentException e) {
            System.out.println("e = " + e);
        }

    }
}
