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

    private static Random random;

    /**
     * 解决SonarQube扫描报错：Save and re-use this "Random".
     */
    static {
        try {
            random = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            log.info("SecureRandom.getInstanceStrong() error, Exception:{}", e.getMessage());
        }
    }

    public static void main(String[] args) {

        for (int i = 0; i < 5; i++) {
            int num = random.nextInt(100);
            System.out.println("random = " + num);
        }
    }
}
