package com.lp.demo.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static java.time.ZoneId.systemDefault;

/**
 * @author lp
 * @date 2022/1/22 17:28
 **/
public class DateTimeUtil {

    /**
     * 获得日期当月第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfMonth(Date date) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        LocalDateTime firstDayOfMonth = LocalDateTime.of(LocalDate.from(dateTime.with(TemporalAdjusters.firstDayOfMonth())), LocalTime.MIN);
        return Date.from(firstDayOfMonth.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获得日期当月最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfMonth(Date date) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        LocalDateTime lastDayOfMonth = LocalDateTime.of(LocalDate.from(dateTime.with(TemporalAdjusters.lastDayOfMonth())), LocalTime.MAX);
        return Date.from(lastDayOfMonth.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获得日期当年第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfYear(Date date) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        LocalDateTime firstDayOfYear = LocalDateTime.of(LocalDate.from(dateTime.with(TemporalAdjusters.firstDayOfYear())), LocalTime.MIN);
        return Date.from(firstDayOfYear.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获得日期当年最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfYear(Date date) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        LocalDateTime lastDayOfYear = LocalDateTime.of(LocalDate.from(dateTime.with(TemporalAdjusters.lastDayOfYear())), LocalTime.MAX);
        return Date.from(lastDayOfYear.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获得日期当天起始时间
     *
     * @param date
     * @return
     */
    public static Date getFirstTimeOfDay(Date date) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        LocalDateTime firstTimeOfDay = LocalDateTime.of(dateTime.toLocalDate(), LocalTime.MIN);
        return Date.from(firstTimeOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获得日期当天结束时间
     *
     * @param date
     * @return
     */
    public static Date getLastTimeOfDay(Date date) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        LocalDateTime lastTimeOfDay = LocalDateTime.of(dateTime.toLocalDate(), LocalTime.MAX);
        return Date.from(lastTimeOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获得日期当周周一
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfWeek(Date date, boolean isCN) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        // TODO lp 法国：周一-周日，其他：周日-周六
        // 根据appUser的locale判断，CN，跟其他
        TemporalField temporalField = WeekFields.of(isCN ? Locale.FRANCE : Locale.ENGLISH).dayOfWeek();
        LocalDateTime firstDayOfWeek = LocalDateTime.of(LocalDate.from(dateTime.with(temporalField, 1)), LocalTime.MIN);
        return Date.from(firstDayOfWeek.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获得日期本周最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfWeek(Date date, boolean isCN) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        // TODO lp 法国：周一-周日，其他：周日-周六
        TemporalField temporalField = WeekFields.of(isCN ? Locale.FRANCE : Locale.ENGLISH).dayOfWeek();
        LocalDateTime lastDayOfWeek = LocalDateTime.of(LocalDate.from(dateTime.with(temporalField, 7)), LocalTime.MAX);
        return Date.from(lastDayOfWeek.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date getFirstDayOfPreviousMonths(Date date, int i) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        LocalDateTime firstDayOfMonth = LocalDateTime.of(LocalDate.from(dateTime.with(TemporalAdjusters.firstDayOfMonth())).minusMonths(i), LocalTime.MIN);
        return Date.from(firstDayOfMonth.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date getLastDayOfPreviousMonths(Date date, int i) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        LocalDateTime lastDayOfMonth = LocalDateTime.of(LocalDate.from(dateTime.with(TemporalAdjusters.lastDayOfMonth())).minusMonths(i), LocalTime.MAX);
        return Date.from(lastDayOfMonth.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取指定年月的第一天
     * @param year
     * @param month
     * @return
     */
    public static Date getFirstDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最小天数
        int firstDay = cal.getMinimum(Calendar.DATE);
        //设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH,firstDay);
        //格式化日期
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return getFirstTimeOfDay(cal.getTime());
    }

    /**
     * 获取指定年月的最后一天
     * @param year
     * @param month
     * @return
     */
    public static Date getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DATE);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return getLastTimeOfDay(cal.getTime());
    }

    /**
     * 是否今天的日期
     *
     * @param date
     * @return
     */
    public static boolean isToday(Date date) {
        LocalDate now = LocalDate.now();
        System.out.println("now = " + now);
        LocalDate localDate = date.toInstant().atZone(systemDefault()).toLocalDate();
        System.out.println("localDate = " + localDate);
        boolean equal = now.isEqual(localDate);
        System.out.println("equal = " + equal);
        boolean before = localDate.isBefore(now);
        System.out.println("before = " + before);
        return equal;
    }


    public static int getDateInterval(Date startTime, Date endTime, ChronoUnit unit) {

        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();

        start.setTime(startTime);
        end.setTime(endTime);

        int i = 0;
        switch (unit) {
            case DAYS:
                // 相差多少秒
                long timeInMillis = end.getTimeInMillis();
                long timeInMillis1 = start.getTimeInMillis();
                long time = startTime.getTime();
                long time1 = endTime.getTime();
                System.out.println("time1 = " + time1);
                System.out.println("time = " + time);
                System.out.println("timeInMillis = " + timeInMillis);
                System.out.println("timeInMillis1 = " + timeInMillis1);

                long l = (timeInMillis - timeInMillis1) / 1000 / 60;
                System.out.println("l = " + l);
                String s = l / 60 + ":" + l % 60;
                System.out.println("s = " + s);
                break;
            case WEEKS:
            case MONTHS:
                int endDay = end.get(Calendar.DAY_OF_MONTH);
                int startDay = start.get(Calendar.DAY_OF_MONTH);
                i = endDay - startDay;
                break;
            case YEARS:
                int endMonth = end.get(Calendar.MONTH);
                int startMonth = start.get(Calendar.MONTH);
                i = endMonth - startMonth;
                break;
            case FOREVER:
                int endYear = end.get(Calendar.YEAR);
                int startYear = start.get(Calendar.YEAR);
                i = endYear - startYear;
                break;
            default:
                break;
        }
        return i;
    }

    public static void main(String[] args) throws ParseException {
        /*Date date = new Date();
        Date firstDayOfWeek = getFirstDayOfWeek(date, true);
        System.out.println("firstDayOfWeek = " + firstDayOfWeek);
        Date lastDayOfWeek = getLastDayOfWeek(date, true);
        System.out.println("lastDayOfWeek = " + lastDayOfWeek);

        Date firstDayOfWeekF = getFirstDayOfWeek(date, false);
        System.out.println("firstDayOfWeekF = " + firstDayOfWeekF);
        Date lastDayOfWeekF = getLastDayOfWeek(date, false);
        System.out.println("lastDayOfWeekF = " + lastDayOfWeekF);

        boolean today = isToday(date);
        System.out.println("today = " + today);

        Date startTime = getFirstTimeOfDay(date);
        System.out.println("firstTimeOfDay = " + startTime);
        LocalDateTime dateTime = LocalDateTime.ofInstant(startTime.toInstant(), systemDefault());
        LocalDateTime afterAnHour = dateTime.plusHours(24);
        System.out.println("afterAnHour = " + afterAnHour);
        Date plusDate = Date.from(afterAnHour.atZone(systemDefault()).toInstant());
        System.out.println("plusDate = " + plusDate);
        boolean today1 = isToday(plusDate);
        System.out.println("today1 = " + today1);

        // 获取日期间隔多少天
        Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2024-02-08 10:15:30");
        int intervalDay = getDateInterval(getFirstDayOfMonth(date1), getLastDayOfMonth(date1), ChronoUnit.MONTHS);
        System.out.println("intervalDay = " + intervalDay);

        startTime = DateTimeUtil.getFirstDayOfYear(date);
        LocalDateTime plusDateTime = LocalDateTime.ofInstant(startTime.toInstant(), systemDefault()).plusYears(1);
        Date endTime = Date.from(plusDateTime.atZone(systemDefault()).toInstant());
        System.out.println("getFirstDayOfYear(date1) = " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime));
        System.out.println("getLastDayOfYear(date1) = " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTime));
        int intervalYear = getDateInterval(startTime, endTime, ChronoUnit.FOREVER);
        System.out.println("intervalYear = " + intervalYear);

        Date firstDayOfYear = DateTimeUtil.getFirstDayOfYear(new Date());
        Date firstTimeOfDay = DateTimeUtil.getFirstTimeOfDay(firstDayOfYear);
        System.out.println("firstDayOfYear = " + firstDayOfYear);
        System.out.println("firstTimeOfDay = " + firstTimeOfDay);
        Date firstDayOfWeek1 = DateTimeUtil.getFirstDayOfWeek(new Date(), true);
        Date firstDayOfMonth = DateTimeUtil.getFirstDayOfMonth(new Date());
        System.out.println("firstDayOfMonth = " + firstDayOfMonth);
        System.out.println("firstDayOfWeek1 = " + firstDayOfWeek1);
*/


        Date start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-02-27 20:45:30");
        Date end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-02-28 10:32:30");
//        int interval = getDateInterval(start, end, ChronoUnit.FOREVER);
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>interval = " + interval);
        int intervalY = getDateInterval(start, end, ChronoUnit.DAYS);
        System.out.println("intervalY = " + intervalY);
//        int intervalM = getDateInterval(start, end, ChronoUnit.MONTHS);
//        System.out.println("intervalM = " + intervalM);

        Date firstDayOfWeek = getFirstDayOfWeek(end, Locale.SIMPLIFIED_CHINESE.getCountry().equalsIgnoreCase("CN"));
        Date lastDayOfWeek = getLastDayOfWeek(end, Locale.SIMPLIFIED_CHINESE.getCountry().equalsIgnoreCase("CN"));
//        String formatF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(firstDayOfWeek);
//        String formatL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastDayOfWeek);
//        System.out.println("formatF = " + formatF);
//        System.out.println("formatL = " + formatL);

        Date nextDay = firstDayOfWeek;
        while (nextDay.before(lastDayOfWeek)) {
            String formatN = new SimpleDateFormat("yyyy-MM-dd").format(nextDay);
            System.out.println("formatN = " + formatN);
            nextDay = Date.from(LocalDateTime.ofInstant(nextDay.toInstant(), systemDefault()).plusDays(1).atZone(systemDefault()).toInstant());
        }
        System.out.println(">>>return : "+ nextDay);


        Date firstDayOfLastMonth = getFirstDayOfPreviousMonths(new Date(), 3);
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(firstDayOfLastMonth);
        System.out.println("format = " + format);

        Date lastDayOfPreviousMonths = getLastDayOfPreviousMonths(new Date(), 3);
        String format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastDayOfPreviousMonths);
        System.out.println("format1 = " + format1);
    }
}
