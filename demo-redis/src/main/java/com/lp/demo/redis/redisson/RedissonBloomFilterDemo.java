package com.lp.demo.redis.redisson;

import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @create 2021/4/3 22:17
 * @author lp
 * @description
 **/
public class RedissonBloomFilterDemo {

    @Autowired
    private Redisson redisson;

    public boolean redissonBloomFilter(String s) {
//        Config config = new Config();
//        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
//        RedissonClient redissonClient = Redisson.create(config);

        RBloomFilter<String> bloomFilter = redisson.getBloomFilter("nameList");
        bloomFilter.tryInit(1000000L, 0.03);
        bloomFilter.add("zhangsan");
        bloomFilter.add("lisi");

        return bloomFilter.contains(s);
    }
}
