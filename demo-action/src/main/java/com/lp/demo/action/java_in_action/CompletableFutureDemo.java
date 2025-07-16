package com.lp.demo.action.java_in_action;

import com.lp.demo.action.java_in_action.CompletableFuture.DefaultValueHandle;
import com.lp.demo.action.java_in_action.CompletableFuture.LogErrorAction;

import java.util.Collections;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    public static void main(String[] args) {
        testThenApply();
        testSupplyAsync();
        testRunAsync();
        testWhenComplete();
        testThenAccept();
        testThenRun();
        testThenCombine();
    }


    /**
     * testThenApply
     * thenApply 当前阶段执行完后，结果作为下一阶段的参数。
     */
    public static void testThenApply() {
        CompletableFuture future = CompletableFuture.supplyAsync(() -> 1)
                .thenApply(i -> ++i)
                .thenApply(i -> i << 2)
                .whenComplete((r,e) -> System.out.println("testThenApply..." + r))
                .exceptionally((e) -> {
                    e.printStackTrace();
                    return null;
                });
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * supplyAsync 有返回值,没有指定Executor的方法会使用ForkJoinPool.commonPool() 作为它的线程池执行异步代码
     * runAsync 没有返回值
     */
    public static void testSupplyAsync() {
        CompletableFuture future = CompletableFuture.supplyAsync(() -> 100 >> 2)
                .thenApplyAsync(i -> i * 10);
        try {
            System.out.println("testSupplyAsync..." + future.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
    public static void testRunAsync() {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
            }
            System.out.println("testRunAsync end ...");
        });

        try {
            System.out.println("testRunAsync..." + future.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * testWhenComplete
     * whenComplete 当前任务线程执行后，如果该线程执行慢没有立即返回结果时，使用whenComplete等待获取处理执行完后的结果，不会像使用get()一样阻塞住线程
     */
    public static void testWhenComplete() {
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

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * testThenAccept
     * thenAccept 接收任务结果（上个任务结果会传过来），并消费，无返回值
     */
    public static void testThenAccept() {
        CompletableFuture.supplyAsync(() -> "testThenAccept...")
                .thenAccept(System.out::println);
    }

    /**
     * testThenRun
     * thenRun 不接受任务结果（上个任务结果不会传过来），只是在上个任务执行完成后执行thenRun方法
     */
    public static void testThenRun() {
        CompletableFuture.supplyAsync(() -> 100)
                .thenRun(() -> System.out.println("testThenRun..."));
    }

    /**
     * thenCombine
     * 有三个操作step1、step2、step3存在依赖关系，其中step3的执行依赖step1和step2的结果
     */
    public static void testThenCombine() {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("执行step1");
            return "step1result";
        }, executor);
        CompletableFuture<String> cf2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("执行step2");
            return "step2result";
        }, executor);
        cf1.thenCombine(cf2, (result1, result2) -> {
            System.out.println(result1 + "," + result2);
            System.out.println("执行step3");
            return "step3result";
        }).thenAccept(result3 -> System.out.println(result3))
                .whenComplete(new LogErrorAction<>("testThenCombine.LogErrorAction", 11, 22, 33))
                .handle(new DefaultValueHandle<>("testThenCombine.DefaultValueHandle", Collections.emptyMap(), 44, null, 66));
    }

}
