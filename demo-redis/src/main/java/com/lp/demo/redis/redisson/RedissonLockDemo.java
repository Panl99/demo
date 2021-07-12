package com.lp.demo.redis.redisson;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @create 2021/4/3 22:18
 * @author lp
 * @description
 **/
@Service
public class RedissonLockDemo {

    @Autowired
    private Redisson redisson;

    public String redissonLock() {
        String lockKey = "lockkey_100";
        String clientId = UUID.randomUUID().toString();

        //获取锁对象
        RLock redissonLock = redisson.getLock(lockKey);
        try {
            //加锁
            redissonLock.lock();
            //业务逻辑

        } finally {
            //释放锁
            redissonLock.unlock();
        }

        return "";
    }
}
