package com.lp.demo.action.java_in_action.函数式编程.泛型和函数式回调;

import lombok.SneakyThrows;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

/**
 * @author lp
 * @date 2022/10/30 22:46
 * @desc 工具类作用：启动时创建N个线程，调用N个线程的回调方法，去通知做一件事情，起到测试线程安全的作用
 * 泛型 和 函数式编程的应用
 * <p>
 * 自定义函数式接口，接收多个参数，返回指定类型结果 （泛型的使用）
 **/
public class ConcurrentStartUtil2<A, B, C> {

    /**
     * 处理的内容放到上下文
     */
    private static final String THREAD_NAME_PREFIX = "multiStart-exec-";
    // 启动标志 信号量
    private CountDownLatch startFlag;
    // 主线程等待工作线程工作完毕 信号量
    private CountDownLatch waitDone;
    // 回调方法
    private MySupplier<A, B, C> mySupplier;
    // 错误处理器
    private Consumer<Exception> errorHandler;


    private ConcurrentHashMap<String, C> resultMap;

    /**
     * 启动方法，带有异常处理
     *
     * @param threadCount 线程数量
     * @param mySupplier  回调
     */
    public static <A, B, C> Map<String, C> start(int threadCount,
                                                 MySupplier<A, B, C> mySupplier,
                                                 A a,
                                                 B b,
                                                 Consumer<Exception> errorHandler) {

        return new ConcurrentStartUtil2<A, B, C>().doStart(threadCount, mySupplier, a, b, errorHandler);
    }

    /**
     * 启动方法，不处理异常
     *
     * @param threadCount 线程数量
     * @param mySupplier  回调
     */
    public static <A, B, C> Map<String, C> start(int threadCount,
                                                 MySupplier<A, B, C> mySupplier,
                                                 A a,
                                                 B b) {

        return new ConcurrentStartUtil2<A, B, C>().doStart(threadCount, mySupplier, a, b, null);
    }

    @SneakyThrows
    public Map<String, C> doStart(int threadCount,
                                  MySupplier<A, B, C> mySupplier,
                                  A a,
                                  B b,
                                  Consumer<Exception> errorHandler) {
        this.startFlag = new CountDownLatch(1);
        this.waitDone = new CountDownLatch(threadCount);
        this.resultMap = new ConcurrentHashMap<>(threadCount);
        this.mySupplier = mySupplier;
        this.errorHandler = errorHandler;

        for (int i = 1; i <= threadCount; i++) {
            // 参数：回调方法，启动标志，完成
            WorkThread<A, B, C> workThread = new WorkThread<>(this, a, b);
            workThread.setName(THREAD_NAME_PREFIX + i);
            workThread.start();
        }
        // 等所有线程创建完成后，启动标志从1减到0，WorkThread的run()会唤醒
        startFlag.countDown();
        waitDone.await();
        return resultMap;
    }

    /**
     * 定义工作线程
     */
    private static class WorkThread<A, B, C> extends Thread {

        private final ConcurrentStartUtil2<A, B, C> context;
        private final A a;
        private final B b;

        private WorkThread(ConcurrentStartUtil2<A, B, C> context,
                           A a,
                           B b) {
            this.context = context;
            this.a = a;
            this.b = b;
        }

        @Override
        @SneakyThrows
        public void run() {
            context.startFlag.await();
            try {
                // 执行回调方法，并保存回调结果
                context.resultMap.put(Thread.currentThread().getName(), context.mySupplier.get(a, b));
            } catch (Exception e) {
                // 如果有异常就抛出去，不去处理
                if (Objects.isNull(context.errorHandler)) {
                    throw e;
                }
                context.errorHandler.accept(e);
            } finally {
                // 主线程等待所有线程执行完毕
                context.waitDone.countDown();
            }
        }
    }

    @FunctionalInterface
    public interface MySupplier<A, B, C> {

        C get(A a, B b);
    }
}
