package com.lp.demo.common.util;

import lombok.extern.slf4j.Slf4j;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

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
    }
}
