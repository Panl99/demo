package com.lp.demo.action.java_in_action;

import com.lp.demo.common.util.ConsoleColorUtil;

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
}
