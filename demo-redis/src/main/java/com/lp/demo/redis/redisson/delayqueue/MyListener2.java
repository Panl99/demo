package com.lp.demo.redis.redisson.delayqueue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 延迟队列监听
 */
@Component
@Slf4j
public class MyListener2 implements RedisDelayedQueueListener<String> {

    @Override
    public void invoke(String data) {
        log.info("MyListener2.invoke: {}", data);
    }
}