package com.lp.demo.common.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * @author lp
 * @date 2021/8/4 15:16
 **/
@Slf4j
public class RandomUtil {

    private static Random random = new SecureRandom();

    /**
     * 解决SonarQube扫描报错：Save and re-use this "Random".
     * [SecureRandom.getInstanceStrong()引发的线程阻塞问题分析](https://blog.csdn.net/weixin_45244678/article/details/106137948)
     */
//    static {
//        try {
//            random = SecureRandom.getInstanceStrong();
//        } catch (NoSuchAlgorithmException e) {
//            log.info("SecureRandom.getInstanceStrong() error, Exception:{}", e.getMessage());
//        }
//    }

    public static void main(String[] args) {

        for (int i = 0; i < 5; i++) {
            int num = random.nextInt(100);
            System.out.println("random = " + num);
        }


        Set<Long> ids = new HashSet<>();
        for (int i = 0; i < 10000; i++) {
            long uniqueId = UniqueIdGenerator.generateUniqueIdBySnow(i);
            ids.add(uniqueId);
            System.out.println("uniqueId = " + uniqueId);
        }
        System.out.println("ids.size() = " + ids.size());

    }

    /**
     * 唯一id
     * 7位数字唯一：唯一id对10^7取模运算
     *
     */
    public static class UniqueIdGenerator {
        private static final int RANDOM_NUMBER_LENGTH = 3;
        private static final long MAX_ID = 10000000L;

        /**
         * 结果会有重复，是因为随机数会重复，或时间戳重复
         * @return
         */
        public static long generateUniqueId() {
            long timestamp = System.currentTimeMillis();
            int randomNumber = new Random().nextInt((int) Math.pow(10, RANDOM_NUMBER_LENGTH));
            String idString = String.format("%016d-%03d", timestamp, randomNumber);
            long id = Long.parseLong(idString.replaceAll("-", ""));
            return id % MAX_ID;
        }

        /**
         * 会重复
         * @param i
         * @return
         */
        public static long generateUniqueIdBySnow(long i) {
            long id = IdUtil.getWorkerId(i, Long.MAX_VALUE);
            return id % MAX_ID;
        }
    }
}
