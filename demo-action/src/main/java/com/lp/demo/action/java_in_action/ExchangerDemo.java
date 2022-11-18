package com.lp.demo.action.java_in_action;

import com.lp.demo.common.util.ConsoleColorUtil;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author lp
 * @date 2022/11/17 14:00
 * @desc 线程间交换数据的Exchanger
 **/
public class ExchangerDemo {

    private static final Exchanger<String> EXCHANGER = new Exchanger<>();
    private static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {
        THREAD_POOL.execute(() -> {
            try {
                String A = "银行流水A"; // A录入银行流水数据
                ConsoleColorUtil.printDefaultColor("1");
                String exchangeA = EXCHANGER.exchange(A);
                ConsoleColorUtil.printDefaultColor("exchangeA : " + exchangeA);
            } catch (InterruptedException e) {
            }
        });

        THREAD_POOL.execute(() -> {
            try {
                String B = "银行流水B"; // B录入银行流水数据
                ConsoleColorUtil.printDefaultColor("2");
                TimeUnit.SECONDS.sleep(3);
                String A = EXCHANGER.exchange("");
                ConsoleColorUtil.printDefaultColor("A和B数据是否一致：" + A.equals(B) + "，A录入的是：" + A + "，B录入是：" + B);
            } catch (InterruptedException e) {
            }
        });

        THREAD_POOL.shutdown();
    }

}
