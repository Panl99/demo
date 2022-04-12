package com.lp.demo.action;

import com.lp.demo.action.spring_in_action.async.AsyncTask;
import com.lp.demo.common.util.ConsoleColorUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

@SpringBootTest
class DemoActionApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    AsyncTask asyncTask;
    @Autowired
    @Qualifier("getExecutor")
    Executor executor;

    @Test
    public void AsyncTaskTest() throws InterruptedException, ExecutionException {
        Future<String> task1 = asyncTask.doTask1();
        Future<String> task2 = asyncTask.doTask2();
        executor.execute(() -> {
            asyncTask.doTask3();
        });
        ConsoleColorUtil.printDefaultColor("Task3 End!");

        while (true) {
            if (task1.isDone() && task2.isDone()) {
                ConsoleColorUtil.printDefaultColor("Task1 result: " + task1.get());
                ConsoleColorUtil.printDefaultColor("Task2 result: " + task2.get());
                break;
            }
            Thread.sleep(1000);
        }

        ConsoleColorUtil.printDefaultColor("All tasks finished.");
    }


}
