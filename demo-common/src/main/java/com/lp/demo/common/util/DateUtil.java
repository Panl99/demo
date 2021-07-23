package com.lp.demo.common.util;

import com.lp.demo.common.enums.ZoneIdEnum;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Set;

/**
 * @author lp
 * @date 2021/5/29 18:28
 * @description
 **/
public class DateUtil {
    public static void main(String[] args) {
        System.out.println("getDate() = " + getDate());
        System.out.println("getDate(caymanZoneId) = " + getDate(ZoneIdEnum.EST.getZoneIdName()));
        System.out.println("getTime() = " + getTime());
        System.out.println("getTime(caymanZoneId) = " + getTime(ZoneIdEnum.EST.getZoneIdName()));
        System.out.println("getDateTime() = " + getDateTime());
        System.out.println("getDateTimeZone() = " + getDateTimeZone());
        System.out.println("getDateTimeZone(caymanZoneId) = " + getDateTimeZone(ZoneIdEnum.EST.getZoneIdName()));
        System.out.println("getDateTimeFormat() = " + getDateTimeFormat());
        System.out.println("getSimpleDateFormat() = " + getSimpleDateFormat());
        System.out.println("getDateInterval(2021, 5, 1) = " + getDateInterval(2021, 5, 1));

        System.out.println("getZoneIds() = " + getZoneIds());
        System.out.println("ZonedDateTimeTransform(14, 03, shanghaiZoneId, caymanZoneId) = " + ZonedDateTimeTransform(12, 03, ZoneIdEnum.CTT.getZoneIdName(), ZoneIdEnum.EST.getZoneIdName()));

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
     * 获取当前日期(指定时区)
     *
     * @return xxxx-xx-xx
     */
    public static String getDate(String zoneId) {
        LocalDate today = LocalDate.now(ZoneId.of(zoneId));
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
     * 获取当前时间点(指定时区)
     *
     * @return xx:xx:xx.xxx
     */
    public static String getTime(String zoneId) {
        LocalTime time = LocalTime.now(ZoneId.of(zoneId));
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
     * 获取当前时间戳（系统默认时区）
     * CTT - Asia/Shanghai
     * @return xxxx-xx-xxTxx:xx:xx.xxx
     */
    public static String getDateTimeZone() {
        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        return now.toString();
    }

    /**
     * 获取当前时间戳（指定时区）
     *
     * @return xxxx-xx-xxTxx:xx:xx.xxx
     */
    public static String getDateTimeZone(String zoneId) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of(zoneId));
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
     * 获取时区
     */
    public static Set<String> getZoneIds() {
        return ZoneId.getAvailableZoneIds();
    }

    /**
     * 时区时间转换
     * @param hour 指定小时
     * @param minute 指定分钟
     * @param sourceZoneId 源分区
     * @param targetZoneId 目标分区
     * @return xxxx-xx-xx xx:xx:xx
     */
    public static String ZonedDateTimeTransform(int hour, int minute, String sourceZoneId, String targetZoneId) {
        ZonedDateTime sourceZoneDateTime = ZonedDateTime.of(LocalDate.now(ZoneId.of(sourceZoneId)), LocalTime.of(hour, minute), ZoneId.of(sourceZoneId));
        ZonedDateTime targetZoneDateTime = sourceZoneDateTime.withZoneSameInstant(ZoneId.of(targetZoneId));
        return targetZoneDateTime.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}
