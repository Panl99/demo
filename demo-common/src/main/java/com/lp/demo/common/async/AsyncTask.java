package com.lp.demo.common.async;

import com.lp.demo.common.util.ConsoleColorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author lp
 * @date 2021/9/11 14:16
 **/
@Slf4j
@Component
public class AsyncTask {
    @Async("getExecutor")
    public Future<String> doTask1() throws InterruptedException{
        ConsoleColorUtil.printDefaultColor("Task1 started.");
        long start = System.currentTimeMillis();
        Thread.sleep(5000);
        long end = System.currentTimeMillis();
        ConsoleColorUtil.printDefaultColor("Task1 finished, time elapsed: "+ (end - start) +" ms.");

        return new AsyncResult<>("Task1 End!");
    }

    @Async("getExecutor")
    public Future<String> doTask2() throws InterruptedException{
        ConsoleColorUtil.printDefaultColor("Task2 started.");
        long start = System.currentTimeMillis();
        Thread.sleep(3000);
        long end = System.currentTimeMillis();

        ConsoleColorUtil.printDefaultColor("Task2 finished, time elapsed: "+ (end - start) +" ms.");

        return new AsyncResult<>("Task2 End!");
    }

    @Async("getExecutor")
    public void doTask3() {
        ConsoleColorUtil.printDefaultColor("Task3 started.");
        long start = System.currentTimeMillis();

        try {
            TimeUnit.SECONDS.sleep(5); // 大于5 下面打印日志不执行
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        ConsoleColorUtil.printDefaultColor("Task3 finished, time elapsed: "+ (end - start) +" ms.");
    }
}
