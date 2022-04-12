package com.lp.demo.action.java_in_action;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @create 2020/10/2 22:03
 * @auther outman
 **/
public class ScheduledThreadPoolExecutorDemo {
    public static AtomicInteger atomicInteger = new AtomicInteger();
    public static void main(String[] args) {
        //call testScheduledThreadPoolExecutor()
        testScheduledThreadPoolExecutor();
    }

    /**
     * 定时任务
     */
    public static void testScheduledThreadPoolExecutor() {
        ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(2);
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        poolExecutor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                System.out.println("poolExecutor"+atomicInteger.incrementAndGet());
            }
        },0,1, TimeUnit.SECONDS);

        executorService.scheduleAtFixedRate(() -> {
            System.out.println("executorService"+atomicInteger.incrementAndGet());
        },1,1, TimeUnit.SECONDS);

    }
}
