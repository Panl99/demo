package com.lp.demo.common.util.hutool;

import cn.hutool.core.lang.SimpleCache;
import com.lp.demo.common.util.ConsoleColorUtil;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author lp
 * @date 2022/12/1 11:16
 * @desc
 **/
public class SimpleCacheDemo {
    public static void main(String[] args) throws InterruptedException {
        ConsoleColorUtil.printDefaultColor("=================test===================");
        test();
    }

    public static void test() throws InterruptedException {
        String key = "test";
        int value = 123;
//        // 初始化map只能查看，不能操作，否则会抛UnsupportedOperationException
//        SimpleCache<String, Integer> simpleCache =
//                new SimpleCache<>(Collections.singletonMap(key, value));
        SimpleCache<String, Integer> simpleCache = new SimpleCache<>();
        simpleCache.put(key, value);

        Integer getValue = simpleCache.get(key);
        System.out.println("getValue = " + getValue);

        Integer remove = simpleCache.remove(key);
        System.out.println("remove = " + remove);

        getValue = simpleCache.get(key);
        System.out.println("getValue after remove = " + getValue);

        TimeUnit.SECONDS.sleep(1);

        Integer put = simpleCache.put(key, value * 10);
        System.out.println("put = " + put);

        getValue = simpleCache.get(key);
        System.out.println("getValue after put = " + getValue);

        Integer put2 = simpleCache.put(key + "2", value * 20);
        System.out.println("put2 = " + put2);

        System.out.println("foreach print 👇");
        simpleCache.iterator().forEachRemaining(System.out::println);

        TimeUnit.SECONDS.sleep(1);

        simpleCache.clear();
        System.out.println("foreach print after clear 👇");
        simpleCache.iterator().forEachRemaining(System.out::println);

    }
}
