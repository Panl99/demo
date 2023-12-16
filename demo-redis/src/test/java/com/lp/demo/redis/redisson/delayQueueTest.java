package com.lp.demo.redis.redisson;

import com.lp.demo.redis.redisson.delayqueue.MyListener;
import com.lp.demo.redis.redisson.delayqueue.RedisDelayedQueue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

/**
 * @author lp
 * @date 2023/12/15 18:25
 * @desc
 **/
@SpringBootTest
public class delayQueueTest {

    @Autowired
    private RedisDelayedQueue redisDelayedQueue;

    @Test
    void test() {
        String id = "just test";

        //将订单id放入延时队列,配置过期监听的处理类
        redisDelayedQueue.add(id, 2, TimeUnit.SECONDS, MyListener.class.getName());
    }
}
