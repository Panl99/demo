package com.lp.demo.common.util;

import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lp
 * @date 2025/2/11 14:30
 * @desc
 **/
@Component
public class IdUtil {

    private static final String SERIAL_ID_REGEX = "\\(([A-Za-z]{2}\\d{4}[0-9a-zA-Z]{4})\\)$";
    private static final String ATOMIC_ID_REGEX = "\\(([0-9a-zA-Z]{4})\\)$";
    private static final int LENGTH_LIMIT = 64;

    private static RedissonClient redissonClient;

    @Autowired
    public void setRedissonClient(RedissonClient redissonClient) {
        IdUtil.redissonClient = redissonClient;
    }

    /**
     * 序列id
     *
     * @param type   系统类型
     * @param prefix 系统前缀
     * @return 2位系统标识 + 4位年月时间 + 4位36进制自增序号
     */
    public static String serialId(String type, String prefix) {
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMM"));
        RAtomicLong atomicLong = redissonClient.getAtomicLong("SERIAL_ID:" + type + ":" + dateStr);
        // 设置月底过期
        if (atomicLong.get() == 0) {
            atomicLong.set(0);
            atomicLong.expireAt(DateTimeUtil.getLastDayOfMonth(new Date()).getTime());
        }
        // 4位36进制自增序号
        String atomicIdStr = Long.toString(atomicLong.incrementAndGet(), Character.MAX_RADIX).toUpperCase();
        if (atomicIdStr.length() < 4) {
            atomicIdStr = String.format("%4s", atomicIdStr).replace(' ', '0');
        }
        return prefix + dateStr + atomicIdStr;
    }

    /**
     * 下一个序列id
     *
     * @param type 系统类型
     * @param s    当前字符串
     * @return
     */
    public static String nextSerialId(String type, String prefix, String s) {
        Pattern pattern = Pattern.compile(SERIAL_ID_REGEX);
        Matcher matcher = pattern.matcher(s);

        if (matcher.find()) {
            String item = matcher.group(1);
            // 只自增最后一个
            int lastIndex = s.lastIndexOf(item);
            if (lastIndex != -1) {
                return s.substring(0, lastIndex) + serialId(type, prefix/*item.substring(1, 3)*/) + s.substring(lastIndex + item.length());
            }
        }
        return buildSerialId(type, prefix, s);
    }

    private static String buildSerialId(String type, String prefix, String s) {
        String serialId = "(" + serialId(type, prefix) + ")";
        String result = s + serialId;
        // 限制长度64
        if (result.length() > LENGTH_LIMIT) {
            result = s.substring(0, LENGTH_LIMIT - serialId.length()) + serialId;
        }
        return result;
    }

    /**
     * 匹配并自增序列id
     *
     * @param input
     * @return
     */
    public static String incrementSerialId(String input) {
        Pattern pattern = Pattern.compile(SERIAL_ID_REGEX);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            String suffix = matcher.group(1);
            // 提取 36 进制部分
            String hexPart = suffix.substring(6);

            // 将 36 进制数字转换为十进制
            long decimalValue = Long.parseLong(hexPart, 36);

            // 自增操作
            decimalValue++;

            // 将十进制数字转换回 36 进制
            String incrementedHex = Long.toString(decimalValue, 36).toUpperCase();

            // 保证自增后的数字是 4 位
            while (incrementedHex.length() < 4) {
                incrementedHex = "0" + incrementedHex;
            }

            // 生成新的字符串，替换自增后的 36 进制数字
            String newSuffix = suffix.substring(0, 6) + incrementedHex;
            return input.substring(0, input.length() - suffix.length() - 2) + "(" + newSuffix + ")";
        } else {
            return input;
        }
    }

    public static void main(String[] args) {
        String serialId = incrementSerialId("test(CP25020001)(CP2502001a)(CP250200cc)");
        System.out.println("serialId = " + serialId);
    }


}
