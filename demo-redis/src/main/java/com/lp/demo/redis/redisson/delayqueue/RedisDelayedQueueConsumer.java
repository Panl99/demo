package com.lp.demo.redis.redisson.delayqueue;

import cn.hutool.core.thread.NamedThreadFactory;
import com.alibaba.fastjson.JSON;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * redis 延时队列初始化
 */
@Component
@Slf4j
public class RedisDelayedQueueConsumer implements ApplicationContextAware { //implements CommandLineRunner {

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 获取应用上下文并获取相应的接口实现类
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, RedisDelayedQueueListener> map = applicationContext.getBeansOfType(RedisDelayedQueueListener.class);
        for (Map.Entry<String, RedisDelayedQueueListener> taskEventListenerEntry : map.entrySet()) {
            String listenerName = taskEventListenerEntry.getValue().getClass().getName();
            //
            consume(listenerName, taskEventListenerEntry.getValue());
        }
    }


//    @Override
//    public void run(String... args) throws Exception {
//
//    }

    /**
     * 启动线程获取队列
     *
     * @param queueName                 队列名称
     * @param redisDelayedQueueListener 任务回调监听
     */
    private <T> void consume(String queueName, RedisDelayedQueueListener redisDelayedQueueListener) {
        RBlockingQueue<T> blockingQueue = redissonClient.getBlockingQueue(queueName);
        RDelayedQueue<T> delayedQueue = redissonClient.getDelayedQueue(blockingQueue);
        //由于此线程需要常驻，可以新建线程，不用交给线程池管理
        Thread thread = new Thread(() -> {
            log.info("启动监听队列线程" + queueName);
            while (true) {
                try {
                    T t = blockingQueue.take();
                    log.info("监听队列线程{},获取到值:{}", queueName, JSON.toJSONString(t));
                    redisDelayedQueueListener.invoke(t);
                } catch (Exception e) {
                    log.info("监听队列线程错误,", e);
                }
            }
        });
        thread.setName(queueName);
        thread.start();
    }


    private static final ScheduledThreadPoolExecutor SCHEDULE = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("RedisDelayedQueue", false));

    private void consume() {
        log.info("RedisDelayedQueueConsumer.consume.");
        SCHEDULE.scheduleWithFixedDelay(() -> {
            String queueName = String.format("prefix:%s", "*");
            Set<String> keys = redisTemplate.keys(queueName);
            if (CollectionUtils.isEmpty(keys)) {
                return;
            }
            keys.forEach(k -> {
                RBlockingQueue<String> blockingQueue = redissonClient.getBlockingQueue(k);
                RDelayedQueue<String> delayedQueue = redissonClient.getDelayedQueue(blockingQueue);
                String msg = null;
                try {
                    msg = blockingQueue.take();
                    if (StringUtil.isEmpty(msg)) {
                        log.error("consume redis delay queue, msg is empty! key: {}", k);
                        return;
                    }
                    // handle msg
                } catch (Exception e) {
                    log.error("consume redis delay queue error, e : ", e);
                }
            });
        }, 1, 5, TimeUnit.SECONDS);
    }


}