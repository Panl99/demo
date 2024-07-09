package com.lp.demo.spring.callback.redis;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.lp.demo.spring.callback.redis.Demo1RedisKeyExpirationListener.TEST_NOTICE_KEY;

/**
 * @author lp
 * @date 2022/10/13 15:09
 * @desc
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
class RedisCallbackTest {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Test
    void timeoutCallbackTest() {
        String name = "title" + "-" + System.currentTimeMillis();
        redisTemplate.opsForValue().set(TEST_NOTICE_KEY + name, System.currentTimeMillis() + "", 20, TimeUnit.SECONDS);
    }

    @Test
    void deleteCallbackTest() {
        String name = "title" + "-" + System.currentTimeMillis();
        redisTemplate.opsForValue().set(TEST_NOTICE_KEY + name, System.currentTimeMillis() + "", 60, TimeUnit.SECONDS);

        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(40 * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(">>>>>delete " + name);
            redisTemplate.delete(TEST_NOTICE_KEY + name);
        });
    }
}