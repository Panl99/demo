package com.lp.demo.common.util;

import java.math.BigInteger;
import java.text.NumberFormat;

/**
 * @author lp
 * @date 2022/11/17 11:00
 * @desc
 **/
public class MathUtil {

    public static void main(String[] args) {
        System.out.println("=================指数运算===================");
        指数运算();

        System.out.println("=================十六进制转二进制===================");
        十六进制转二进制();
    }

    public static void 指数运算() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(false);
        numberFormat.setMaximumFractionDigits(10);

        int 指数 = 0;
        String format = numberFormat.format(-2 * Math.pow(10, 指数));
        System.out.println("format = " + format);

        指数 = 3;
        format = numberFormat.format(-2 * Math.pow(10, 指数));
        System.out.println("format = " + format);

        指数 = 3;
        format = numberFormat.format(2 * Math.pow(10, 指数));
        System.out.println("format = " + format);

        指数 = -3;
        format = numberFormat.format(2 * Math.pow(10, 指数));
        System.out.println("format = " + format);
    }

    public static void 十六进制转二进制() {
        String hex = "01020304";
        // 小端最高位
        String substring = hex.substring(hex.length() - 2);
        System.out.println("substring = " + substring);
        // 转二进制
        String s2 = new BigInteger(substring, 16).toString(2);
        System.out.println("s2 = " + s2);
        // 二进制最高位是0
        int i1 = s2.length() % 2;
        System.out.println("i1 = " + i1);
    }


}
