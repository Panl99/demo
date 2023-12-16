package com.lp.demo.redis.redisson.delayqueue;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis 延时队列
 */
@Component
@Slf4j
public class RedisDelayedQueue {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 添加对象进延时队列
     *
     * @param data      添加数据
     * @param delay     延时时间
     * @param timeUnit  时间单位
     * @param queueName 队列名称
     * @param <T>
     */
    public <T> void add(T data, long delay, TimeUnit timeUnit, String queueName) {
        log.info("RedisDelayedQueue.add, queueName: {}, delayTime: {}, timeUnit: {}, data: {}", queueName, delay, timeUnit, data);
        RBlockingQueue<T> blockingFairQueue = redissonClient.getBlockingQueue(queueName);
        RDelayedQueue<T> delayedQueue = redissonClient.getDelayedQueue(blockingFairQueue);
        delayedQueue.offer(data, delay, timeUnit);
    }

    /**
     * 消费队列消息
     *
     * @param queueName 队列名
     * @throws InterruptedException
     */
    public void consume(String queueName) throws InterruptedException {
        RBlockingQueue<String> blockingQueue = redissonClient.getBlockingQueue(queueName);
        RDelayedQueue<String> delayedQueue = redissonClient.getDelayedQueue(blockingQueue);
        String msg = blockingQueue.take();
        // handle msg
    }

}