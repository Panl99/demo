package com.lp.demo.common.util;

import com.lp.demo.common.enums.ZoneIdEnum;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.Calendar;
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

        System.out.println("calculateTime()");
        calculateTime("2021-09-15 18:10:36", -5);

        // 时间单位测试
        chronoUnitTest();
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

    public static void calculateTime(String date, int interval) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(format.parse(date));
        } catch (ParseException e) {
            ConsoleColorUtil.printSingleColor(e.getMessage(), ConsoleColorUtil.ConsoleColorCodeEnum.RED.getColorCode(), ConsoleColorUtil.ConsoleStyleCodeEnum.UNDERSCORE.getStyleCode());
            return;
        }

        ConsoleColorUtil.printDefaultColor(format.format(calendar.getTime()));

        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + interval);
        ConsoleColorUtil.printDefaultColor(format.format(calendar.getTime()));

        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + interval);
        ConsoleColorUtil.printDefaultColor(format.format(calendar.getTime()));
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


    /**
     * TemporalAdjusters用法 TODO
     */


    /**
     * ChronoUnit：计时单位枚举
     * @since 1.8
     */
    public static void chronoUnitTest() {
        ConsoleColorUtil.printDefaultColor(">>>>>>>>>>>>>>>>>>>>>>>>>>>>chronoUnitTest<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        // 当前日期
        LocalDate today = LocalDate.now();
        ConsoleColorUtil.printDefaultColor("Current date: " + today);
        // 1周后
        LocalDate nextWeek = today.plus(1, ChronoUnit.WEEKS);
        ConsoleColorUtil.printDefaultColor("Next week: " + nextWeek);
        // 1月后
        LocalDate nextMonth = today.plus(1, ChronoUnit.MONTHS);
        ConsoleColorUtil.printDefaultColor("Next month: " + nextMonth);
        // 1年后
        LocalDate nextYear = today.plus(1, ChronoUnit.YEARS);
        ConsoleColorUtil.printDefaultColor("Next year: " + nextYear);
        // 10年后
        LocalDate nextDecade = today.plus(1, ChronoUnit.DECADES);
        ConsoleColorUtil.printDefaultColor("Date after ten year: " + nextDecade);

        // 当前日期时间
        LocalDateTime now = LocalDateTime.now();
        ConsoleColorUtil.printDefaultColor("Current date time: " + now);
        // 明天此时
        LocalDateTime nextDay = now.plusDays(1);
        ConsoleColorUtil.printDefaultColor("Next day: " + nextDay);
        // 下周此时
        LocalDateTime nextWeekTime = now.plusWeeks(1);
        ConsoleColorUtil.printDefaultColor("This time next week: " + nextWeekTime);
        // 5小时后
        LocalDateTime fiveHoursLater = now.plusHours(5);
        ConsoleColorUtil.printDefaultColor("5 hours later: " + fiveHoursLater);
        // 30分钟后
        LocalDateTime thirtyMinutesLater = now.plusMinutes(30);
        ConsoleColorUtil.printDefaultColor("30 minutes later: " + thirtyMinutesLater);
        // 30秒后
        LocalDateTime thirtySecondsLater = now.plusSeconds(30);
        ConsoleColorUtil.printDefaultColor("30 seconds later: " + thirtySecondsLater);
        // 永远
//        LocalDateTime forever = now.plus(3, ChronoUnit.FOREVER);
//        ConsoleColorUtil.printDefaultColor("forever: " + forever);
        ConsoleColorUtil.printDefaultColor("forever: " + ChronoUnit.FOREVER.getDuration());
    }
}
