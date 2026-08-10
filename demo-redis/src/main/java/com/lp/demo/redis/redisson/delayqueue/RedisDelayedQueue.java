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

    private static final String DELAYED_QUEUE_TIMEOUT_PREFIX = "redisson_delay_queue_timeout:";

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



    /**
     * 判断队列中（包括未到期和已到期）是否存在该消息
     * 注意：由于序列化问题，此方法只能用于基本类型或确保 equals/hashCode 正确的对象
     *
     * @param queueName 队列名
     * @param message   消息内容
     */
    public static <T> boolean contains(String queueName, T message) {
        RScoredSortedSet<T> timeoutSet = redissonClient.getScoredSortedSet(DELAYED_QUEUE_TIMEOUT_PREFIX + queueName);
        if (timeoutSet.contains(message)) {
            return true;
        }
        RBlockingQueue<T> blockingQueue = redissonClient.getBlockingQueue(queueName);
        return blockingQueue.contains(message);
    }

}