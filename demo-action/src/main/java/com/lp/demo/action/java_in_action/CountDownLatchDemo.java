package com.lp.demo.action.java_in_action;

import com.lp.demo.common.util.ConsoleColorUtil;
import lombok.SneakyThrows;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @create 2021/3/16 20:39
 * @auther lp
 * @description
 **/
public class CountDownLatchDemo {

//    private static final CountDownLatch COUNT = new CountDownLatch(2);

    public static void main(String[] args) throws InterruptedException {
        ConsoleColorUtil.printDefaultColor("=================阻塞执行===================");
        阻塞执行(); // 1 2 3 4

        ConsoleColorUtil.printDefaultColor("=================定时阻塞执行===================");
        定时阻塞执行(); // 11 22 44 33

    }

    public static void 阻塞执行() throws InterruptedException {
        CountDownLatch COUNT = new CountDownLatch(2);
        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                ConsoleColorUtil.printDefaultColor("1");
                COUNT.countDown();
                ConsoleColorUtil.printDefaultColor("2");
                Thread.sleep(3000);
                ConsoleColorUtil.printDefaultColor("3");
                COUNT.countDown();
            }
        }).start();
        // COUNT计数未归为0的话，此处会一直阻塞
        COUNT.await();
        ConsoleColorUtil.printDefaultColor("4");
    }

    public static void 定时阻塞执行() throws InterruptedException {
        CountDownLatch COUNT = new CountDownLatch(2);
        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                ConsoleColorUtil.printDefaultColor("11");
                COUNT.countDown();
                ConsoleColorUtil.printDefaultColor("22");
                Thread.sleep(5000);
                ConsoleColorUtil.printDefaultColor("33");
                COUNT.countDown();
            }
        }).start();
        // COUNT计数未归为0的话，此处会阻塞指定时长后执行后面的逻辑，并返回false。
        boolean await = COUNT.await(3, TimeUnit.SECONDS);
        ConsoleColorUtil.printDefaultColor("await : " + await);
        ConsoleColorUtil.printDefaultColor("44");
    }

}
