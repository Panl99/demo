package com.lp.demo.common.util;

import com.lp.demo.common.enums.ZoneIdEnum;

import java.text.DateFormat;
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
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

/**
 * @author lp
 * @date 2021/5/29 18:28
 * @description
 **/
public class DateUtil {

    private static final String testTime = "2021-09-15 18:10:36";
    private static final String cstTime = "Wed Nov 10 11:42:54 CST 2021";
    private static final String format = "yyyy-MM-dd HH:mm:ss";

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
        calculateTime(testTime, -5);

        // 时间单位测试
        chronoUnitTest();
        // Date与LocalDateTime转换测试
        dateToLocalDateTimeTest();

        // CST时间转GMT时间
        ConsoleColorUtil.printSingleColor(">>>>>>>>>>>>>>>>>> gmtTime <<<<<<<<<<<<<<<<", ConsoleColorUtil.ConsoleColorCodeEnum.BLUE.getColorCode(), 1);
        covertCST2GMT(cstTime);

        // 本年、本月、当天的开始、结束时间
        getFirstOrLastTime();

        // 16进制时间转换
        ConsoleColorUtil.printSingleColor(">>>>>>>>>>>>>>>>>> HexTime <<<<<<<<<<<<<<<<", ConsoleColorUtil.ConsoleColorCodeEnum.BLUE.getColorCode(), 1);
        String unixTime = dateTo4byteHexTime(testTime, format);
        ConsoleColorUtil.printDefaultColor("unixTime = " + unixTime);
        String formatTime = hexTime4ByteToDate(unixTime, format);
        ConsoleColorUtil.printDefaultColor("formatTime = " + formatTime);
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
     * CST时间转GMT时间
     * @param cstTime Date的String类型时间
     * @return
     */
    public static String covertCST2GMT(String cstTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long parseTime = Date.parse(cstTime.replace(" CST ", " GMT+0800 "));
        String gmtTime = sdf.format(new Date(parseTime));
        ConsoleColorUtil.printDefaultColor("cstTime = "+ sdf.format(new Date(cstTime)));
        ConsoleColorUtil.printDefaultColor("gmtTime = "+ gmtTime);
        return gmtTime;
    }


    /**
     * TemporalAdjusters用法 TODO
     */


    /**
     * ChronoUnit：计时单位枚举
     * @since 1.8
     */
    public static void chronoUnitTest() {
        ConsoleColorUtil.printSingleColor(">>>>>>>>>>>>>>>>>> chronoUnitTest <<<<<<<<<<<<<<<<", ConsoleColorUtil.ConsoleColorCodeEnum.BLUE.getColorCode(), 1);

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

    public static void dateToLocalDateTimeTest() {
        ConsoleColorUtil.printSingleColor(">>>>>>>>>>>>>>>>>> dateToLocalDateTime <<<<<<<<<<<<<<<<", ConsoleColorUtil.ConsoleColorCodeEnum.BLUE.getColorCode(), 1);

        Date date = new Date();
        ConsoleColorUtil.printDefaultColor("date: "+ date);

        // Date转为LocalDateTime
        LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        ConsoleColorUtil.printDefaultColor("dateTime: "+ dateTime);

        LocalDateTime plusDateTime = dateTime.plus(1, ChronoUnit.DAYS);
        ConsoleColorUtil.printDefaultColor("plusDateTime: "+ plusDateTime);

        // LocalDateTime转为Date
        Date plusDate = Date.from(plusDateTime.atZone(ZoneId.systemDefault()).toInstant());
        ConsoleColorUtil.printDefaultColor("plusDate: "+ plusDate);

        //
        getExpireTime(date, 2, ChronoUnit.HOURS);
    }


    private static Date getExpireTime(Date date, Integer value, ChronoUnit unit) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        LocalDateTime dateTimePlus = dateTime.plus(value, unit);
        ConsoleColorUtil.printDefaultColor("dateTimePlus = "+ dateTimePlus);

        // LocalDateTime转为Date
        Date datePlus = Date.from(dateTimePlus.atZone(ZoneId.systemDefault()).toInstant());
        ConsoleColorUtil.printDefaultColor("datePlus = "+ datePlus);
        return datePlus;
    }

    public static void getFirstOrLastTime() {
        ConsoleColorUtil.printSingleColor(">>>>>>>>>>>>>>>>>> getFirstOrLastTime <<<<<<<<<<<<<<<<", ConsoleColorUtil.ConsoleColorCodeEnum.BLUE.getColorCode(), 1);

        // 获取今年第一天及最后一天
        LocalDateTime firstDayOfYear = LocalDateTime.of(LocalDate.from(LocalDateTime.now().with(TemporalAdjusters.firstDayOfYear())), LocalTime.MIN);
        LocalDateTime lastDayOfYear = LocalDateTime.of(LocalDate.from(LocalDateTime.now().with(TemporalAdjusters.lastDayOfYear())), LocalTime.MAX);

        // 获取当前月第一天及最后一天
        LocalDateTime firstDayOfMonth = LocalDateTime.of(LocalDate.from(LocalDateTime.now().with(TemporalAdjusters.firstDayOfMonth())), LocalTime.MIN);
        LocalDateTime lastDayOfMonth = LocalDateTime.of(LocalDate.from(LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth())), LocalTime.MAX);

        // 获取当天的起始时间
        LocalDateTime firstTimeOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime lastTimeOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        ConsoleColorUtil.printDefaultColor("firstDayOfYear = "+ firstDayOfYear
                + "\n lastDayOfYear = "+ lastDayOfYear
                + "\n firstDayOfMonth = "+ firstDayOfMonth
                + "\n lastDayOfMonth = "+ lastDayOfMonth
                + "\n firstTimeOfDay = "+ firstTimeOfDay
                + "\n lastTimeOfDay = "+ lastTimeOfDay);

        // LocalDateTime转为Date
        Date date = Date.from(firstDayOfMonth.atZone(ZoneId.systemDefault()).toInstant());
        ConsoleColorUtil.printDefaultColor("date format for firstDayOfMonth = "+ date);
    }


    /**
     * 4字节16进制时间戳 转化成正常时间格式
     *
     * @param hexTime
     * @param format
     * @return
     */
    public static String hexTime4ByteToDate(String hexTime, String format) {
        DateFormat df = new SimpleDateFormat(format);
        long six = Long.parseLong(hexTime,16);
        String time = String.valueOf(six * 1000);
        return df.format(new Date(Long.parseLong(time)));
    }

    //2021-09-15 18:10:36 -> 6141C69C
    public static String dateTo4byteHexTime(String date, String format) {
        StringBuilder unixTime = new StringBuilder();
        try {
            DateFormat df = new SimpleDateFormat(format);
            long time = df.parse(date).getTime();
            String hex = Long.toHexString(time/1000);
            StringBuilder zero = new StringBuilder();
            for (int i = 0; i < 8; i++) {
                if (hex.length() < 8) {
                    zero.append("0");
                }
            }
            unixTime = zero.append(hex);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return unixTime.toString().toUpperCase();

    }
}
