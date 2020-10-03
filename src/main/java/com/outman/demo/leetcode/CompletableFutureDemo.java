package com.outman.demo.leetcode;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @create 2020/10/2 22:08
 * @auther outman
 * 异步非阻塞
 * thenApply 当前阶段执行完后，结果作为下一阶段的参数。
 * thenApplyAsync
 * supplyAsync 有返回值,没有指定Executor的方法会使用ForkJoinPool.commonPool() 作为它的线程池执行异步代码
 * runAsync 没有返回值
 * whenComplete 当前任务线程执行后，如果该线程执行慢没有立即返回结果时，使用whenComplete等待获取处理执行完后的结果，不会像使用get()一样阻塞住线程
 * thenAccept 接收任务结果（上个任务结果会传过来），并消费，无返回值
 * thenRun 不接受任务结果（上个任务结果不会传过来），只是在上个任务执行完成后执行thenRun方法
 **/

public class CompletableFutureDemo {
    public static void main(String[] args) throws Exception {
        testThenApply();
        testSupplyAsync();
        testRunAsync();
        testWhenComplete();
        testThenAccept();
        testThenRun();
    }


    /**
     * testThenApply
     * thenApply 当前阶段执行完后，结果作为下一阶段的参数。
     */
    public static void testThenApply() throws Exception {
        CompletableFuture future = CompletableFuture.supplyAsync(() -> 1)
                .thenApply(i -> ++i)
                .thenApply(i -> i << 2)
                .whenComplete((r,e) -> System.out.println("testThenApply..." + r))
                .exceptionally((e) -> {
                    e.printStackTrace();
                    return null;
                });
        future.get();
    }

    /**
     * supplyAsync 有返回值,没有指定Executor的方法会使用ForkJoinPool.commonPool() 作为它的线程池执行异步代码
     * runAsync 没有返回值
     */
    public static void testSupplyAsync() throws Exception{
        CompletableFuture future = CompletableFuture.supplyAsync(() -> 100 >> 2)
                .thenApplyAsync(i -> i * 10);
        System.out.println("testSupplyAsync..." + future.get());
    }
    public static void testRunAsync() throws Exception {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
            }
            System.out.println("testRunAsync end ...");
        });
        System.out.println("testRunAsync..." + future.get());
    }

    /**
     * testWhenComplete
     * whenComplete 当前任务线程执行后，如果该线程执行慢没有立即返回结果时，使用whenComplete等待获取处理执行完后的结果，不会像使用get()一样阻塞住线程
     */
    public static void testWhenComplete() throws Exception {
        CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
            }
            if(new Random().nextInt() % 2 >= 0) {
                int i = 12 / 0;
            }
            System.out.println("testWhenComplete...");
        })
                .whenComplete((r,e) -> System.out.println("testWhenComplete执行完成！res=" + r))
                .exceptionally(e -> {
                    System.out.println("testWhenComplete执行失败！" + e.getMessage());
                    return null;
                });

        TimeUnit.SECONDS.sleep(2);
    }

    /**
     * testThenAccept
     * thenAccept 接收任务结果（上个任务结果会传过来），并消费，无返回值
     */
    public static void testThenAccept() throws Exception {
        CompletableFuture.supplyAsync(() -> "testThenAccept...")
                .thenAccept(System.out::println);
    }

    /**
     * testThenRun
     * thenRun 不接受任务结果（上个任务结果不会传过来），只是在上个任务执行完成后执行thenRun方法
     */
    public static void testThenRun() throws Exception {
        CompletableFuture.supplyAsync(() -> 100)
                .thenRun(() -> System.out.println("testThenRun..."));
    }

}
