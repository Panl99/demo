package com.lp.demo.redis.jedis;

import redis.clients.jedis.Jedis;

import java.util.Collections;

/**
 * @author lp
 * @create 2021/4/9 23:08
 * @description
 **/
public class JedisLockDemo {

    private static Jedis jedis = new Jedis("127.0.0.1");

    private static final Long SUCCESS = 1L;

    /**
     * 加锁
     * 不存在则保存返回1，加锁成功。如果已经存在则返回0，加锁失败。
     */
    public boolean tryLock(String key, String requestId) {
        return SUCCESS.equals(jedis.setnx(key, requestId));
    }

    /**
     * 加锁，带过期时间
     * 存在，则覆盖
     * @param key
     * @param requestId
     * @param expireTime 过期时间
     * @return
     */
    public boolean tryLock(String key, String requestId, int expireTime) {
        //使用jedis的api，保证原子性
        String result = jedis.setex(key, expireTime, requestId);
        //返回OK则表示加锁成功
        return "OK".equals(result);
    }


    /**
     * 删除key的lua脚本，先比较requestId是否相等，相等则删除
     */
    private static final String DEL_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    /**
     * 解锁
     */
    public boolean unLock(String key, String requestId) {
        //删除成功表示解锁成功
        Long result = (Long) jedis.eval(DEL_SCRIPT, Collections.singletonList(key), Collections.singletonList(requestId));
        return SUCCESS.equals(result);
    }
}
