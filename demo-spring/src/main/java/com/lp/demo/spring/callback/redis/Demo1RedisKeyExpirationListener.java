package com.lp.demo.spring.callback.redis;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Component
public class Demo1RedisKeyExpirationListener extends RedisKeyExpirationListener {

    public static final String TEST = "TEST:";
    public static final String TEST_NOTICE_KEY = TEST + "NOTICE:";

    @Autowired
    StringRedisTemplate redisTemplate;
//    @Autowired
//    RedissonClient redissonClient;


    public Demo1RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        if (!expiredKey.startsWith(TEST)) {
            return;
        }
//        RLock lock = redissonClient.getLock(expiredKey);
//
//        try {
//            if (lock.tryLock(1, 3, TimeUnit.SECONDS)) {
                System.out.println("Demo1 redis key expiration event, key = " + expiredKey);
                // 对key进行过滤
                if (expiredKey.startsWith(TEST_NOTICE_KEY)) {
                    String name = expiredKey.substring(TEST_NOTICE_KEY.length());
                    System.out.println("name = " + name);
                }
//            }
//        } catch (InterruptedException e) {
//            System.out.println("Demo1 redis key expiration event error, e: " + e);
//        } finally {
//            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
//                lock.unlock();
//            }
//        }
    }

}