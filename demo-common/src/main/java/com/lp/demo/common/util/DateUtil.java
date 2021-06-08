package com.lp.demo.common.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author lp
 * @date 2021/5/29 18:28
 * @description
 **/
public class DateUtil {
    public static void main(String[] args) {


        System.out.println(getTime());
        System.out.println(getDate());

        System.out.println(getDateTime());
        System.out.println(getDateTimeZone());
        System.out.println(getDateTimeFormat());
        System.out.println(getSimpleDateFormat());

        System.out.println(getDateInterval(2021, 5, 1));
    }

    /**
     * 获取当前日期
     *
     * @return xxxx-xx-xx
     */
    public static String getDate() {
        LocalDate today = LocalDate.now();
        return today.toString();
    }

    /**
     * 获取当前时间点
     *
     * @return xx:xx:xx.xxx
     */
    public static String getTime() {
        LocalTime time = LocalTime.now();
        return time.toString();
    }

    /**
     * 获取当前时间戳（不带时区）
     *
     * @return xxxx-xx-xxTxx:xx:xx.xxx
     */
    public static String getDateTime() {
        LocalDateTime now = LocalDateTime.now();
        return now.toString();
    }

    /**
     * 获取当前时间戳（带时区）
     * CTT - Asia/Shanghai
     * @return xxxx-xx-xxTxx:xx:xx.xxx
     */
    public static String getDateTimeZone() {
        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        return now.toString();
    }

    /**
     * 获取当前时间戳
     *
     * @return xxxx/xx/xx xx:xx:xx.xxx
     */
    public static String getDateTimeFormat() {
        LocalDateTime now = LocalDateTime.now();
        String nowTime = now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss:SSS"));
        return nowTime;
    }


    /**
     * 获取当前时间戳
     *
     * @return xxxx-xx-xx xx:xx:xx.xxx
     */
    public static String getSimpleDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date());
    }

    /**
     * 获取指定日期距今天多少天
     *
     * @param year  指定年
     * @param month 指定月
     * @param day   指定日
     * @return 今天 - 指定日期（今天30号，指定1号，返回29）
     */
    public static int getDateInterval(int year, int month, int day) {
        Period interval = Period.between(LocalDate.of(year, month, day), LocalDate.now());
        return interval.getDays();
    }

    /**
     * TemporalAdjusters用法 TODO
     */

    /**
     * 时区
     */

}
