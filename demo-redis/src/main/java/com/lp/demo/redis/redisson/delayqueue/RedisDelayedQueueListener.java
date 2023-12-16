package com.lp.demo.redis.redisson.delayqueue;

/**
 * redis 队列事件监听,需要实现这个方法
 * @param <T>
 */
public interface RedisDelayedQueueListener<T> {

    /**
     * 执行方法
     * @param t
     */
    void invoke(T t);
}