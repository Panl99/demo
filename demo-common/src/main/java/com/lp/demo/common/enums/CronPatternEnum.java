package com.lp.demo.common.enums;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public enum CronPatternEnum {

    /**
     * corn
     */
    CRON_ACCURATE("ss mm HH dd MM ? yyyy"),

    // 每天 %d 点
    CRON_N_OCLOCK_EVERYDAY("0 0 %d * * ? *"),

    //
    CRON_9_OCLOCK("0 0 9 dd MM ? yyyy"),

    ;

    private final String content;

    CronPatternEnum(String format) {
        this.content = format;
    }

    public String getContent() {
        return content;
    }

    public String format(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(this.content);
        try {
            return sdf.format(date);
        } catch (Exception e) {
            log.error("Date format error! date: {}, e: ", date, e);
        }
        return "";
    }

    public Date parse(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(this.content);
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            log.error("Date parse error! dateStr: {}, e: ", dateStr, e);
        }
        return null;
    }

    public static void main(String[] args) {
        Date tomorrow = DateUtil.tomorrow();
        String tomorrow9Oclock = CronPatternEnum.CRON_9_OCLOCK.format(tomorrow);
        System.out.println("tomorrow9Oclock = " + tomorrow9Oclock);

        String everyday10Oclock = String.format(CronPatternEnum.CRON_N_OCLOCK_EVERYDAY.getContent(), 10);
        System.out.println("everyday10Oclock = " + everyday10Oclock);
    }
}
