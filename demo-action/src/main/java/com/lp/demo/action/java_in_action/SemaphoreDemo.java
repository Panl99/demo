package com.lp.demo.action.java_in_action;

import com.lp.demo.common.util.ConsoleColorUtil;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author lp
 * @date 2021/3/16 23:05
 * @desc 控制并发线程数的Semaphore
 **/
public class SemaphoreDemo {
    private static final int THREAD_COUNT = 5;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_COUNT);
    private static Semaphore s = new Semaphore(2); // 最多允许2个线程并发执行

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < THREAD_COUNT; i++) {
            threadPool.execute(() -> {
                try {
                    ConsoleColorUtil.printDefaultColor("1");
                    s.acquire(); // 获取一个许可证
                    Thread.sleep(3000);
                    ConsoleColorUtil.printDefaultColor("2");
                    s.release(); // 使用完后释放许可证
                    ConsoleColorUtil.printDefaultColor("3");
                } catch (InterruptedException e) {
                }
            });
        }

        Thread.sleep(1000);
        ConsoleColorUtil.printDefaultColor("4");
        boolean tryAcquire = s.tryAcquire();
        ConsoleColorUtil.printDefaultColor("尝试获取许可证 = " + tryAcquire);

        int availablePermits = s.availablePermits();
        ConsoleColorUtil.printDefaultColor("此信号量中当前可用的许可证数 = " + availablePermits);

        int queueLength = s.getQueueLength();
        ConsoleColorUtil.printDefaultColor("正在等待获取许可证的线程数 = " + queueLength);

        boolean hasQueuedThreads = s.hasQueuedThreads();
        ConsoleColorUtil.printDefaultColor("是否有线程正在等待获取许可证 = " + hasQueuedThreads);

        threadPool.shutdown();
        ConsoleColorUtil.printDefaultColor("5");
    }

    /**
     * 停车场提示牌
     * 1、停车场容纳总停车量4。
     * 2、当一辆车进入停车场后，显示牌的剩余车位数响应的减1.
     * 3、每有一辆车驶出停车场后，显示牌的剩余车位数响应的加1。
     * 4、停车场剩余车位不足时，车辆只能在外面等待。
     */
    static class CarTest {

        // 停车场同时容纳的车辆4
        private static final Semaphore SEMAPHORE = new Semaphore(4);
        // 多少辆车同时进入
        private static final int CONCURRENT_NUM = 20;

        public static void main(String[] args) {
            for (int i = 0; i < CONCURRENT_NUM; i++) {

                new Thread(() -> {
                    try {
                        ConsoleColorUtil.printDefaultColor("=====" + Thread.currentThread().getName() + "来到停车场");
                        if (SEMAPHORE.availablePermits() == 0) {
                            ConsoleColorUtil.printDefaultColor("车位不足!");
                        }

                        SEMAPHORE.acquire(); // 获取令牌尝试进入停车场
                        ConsoleColorUtil.printDefaultColor(">>>>>" + Thread.currentThread().getName() + "成功进入停车场");

                        Thread.sleep(new Random().nextInt(5000)); // 模拟车辆在停车场停留的时间

                        ConsoleColorUtil.printDefaultColor("<<<<<" + Thread.currentThread().getName() + "驶出停车场");
                        SEMAPHORE.release(); // 释放令牌，腾出停车场车位
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }, i + "号车").start();
            }
        }
    }
}
