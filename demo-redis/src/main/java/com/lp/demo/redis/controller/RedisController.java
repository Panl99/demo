package com.lp.demo.redis.controller;

import com.lp.demo.redis.redisson.RedissonBloomFilterDemo;
import com.lp.demo.redis.redisson.RedissonLockDemo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @create 2021/4/3 22:24
 * @auther lp
 * @description
 **/

@RestController
@Slf4j
@RequestMapping("/redis")
public class RedisController {

    @RequestMapping("/lock")
    public String redisLock() {

        RedissonLockDemo redissonLock = new RedissonLockDemo();
        return redissonLock.redissonLock();

    }

    @RequestMapping("/bloomfilter")
    public boolean redisBloomFilter(String s) {
        RedissonBloomFilterDemo redissonBloomFilter = new RedissonBloomFilterDemo();
        return redissonBloomFilter.redissonBloomFilter(s);
    }

}
