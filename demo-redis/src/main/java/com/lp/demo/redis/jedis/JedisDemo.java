package com.lp.demo.redis.jedis;

import redis.clients.jedis.Jedis;

/**
 * @create 2021/3/20 0:11
 * @author lp
 * @description
 **/
public class JedisDemo {
    private static String LOCAL_HOST = "localhost";
    public static void main(String[] args) {
        Jedis jedis = new Jedis(LOCAL_HOST);
        System.out.println("redis link success");
        System.out.println("jedis running: "+ jedis.ping());
    }

    /**
     * 加互斥锁，防止缓存击穿
     */
//    public String getData(String key) {
//        String value = redis.get(key);
//        return value;
//    }
}
