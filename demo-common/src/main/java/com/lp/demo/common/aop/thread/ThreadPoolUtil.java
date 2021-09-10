package com.lp.demo.common.aop.thread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @link https://github.com/Panl99/codebook/blob/master/java/java.md#线程池
 * @from brcc
 */
@Slf4j
public class ThreadPoolUtil {

    private static ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

    static {
        executor.setThreadNamePrefix("thread-pool-");
        // 线程池中核心线程数
        executor.setCorePoolSize(2);
        // 线程池中最大线程数
        executor.setMaxPoolSize(2);
        // 队列容量
        executor.setQueueCapacity(5000);
        // 关闭时等待正在执行的任务执行结束
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 默认是0不等待，现设置为5秒
        executor.setAwaitTerminationSeconds(5);
        // 当前线程数超过 corePoolSize 大小时，空闲线程的存活时间10分钟
        executor.setKeepAliveSeconds(600);
        // 拒绝策略：如果被丢弃的线程任务未关闭，则执行该线程任务。（核心线程数被用完且阻塞队列已排满时调用 ）
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
    }

    public static void submitTask(Runnable task) {
        executor.submit(task);
    }

    public static void stop() {
        if (executor != null) {
            executor.shutdown();
        }
    }
}
