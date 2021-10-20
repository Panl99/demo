package com.lp.demo.common.service.impl;

import com.lp.demo.common.service.ThreadPoolService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author lp
 * @date 2021/10/20 15:10
 *
 * 从线程池的实现可以看到，当客户端调用execute(Job)方法时，会不断地向任务列表jobs中添加Job，而每个工作者线程会不断地从jobs上取
 * 出一个Job进行执行，当jobs为空时，工作者线程进入等待状态。
 *
 * 添加一个Job后，对工作队列jobs调用了其notify()方法，而不是notifyAll()方法，因为能够确定有工作者线程被唤醒，这时使用notify()
 * 方法将会比notifyAll()方法获得更小的开销（避免将等待队列中的线程全部移动到阻塞队列中）。
 *
 * 可以看到，线程池的本质就是使用了一个线程安全的工作队列连接工作者线程和客户端线程，客户端线程将任务放入工作队列后便返回，
 * 而工作者线程则不断地从工作队列上取出工作并执行。当工作队列为空时，所有的工作者线程均等待在工作队列上，
 * 当有客户端提交了一个任务之后会通知任意一个工作者线程，随着大量的任务被提交，更多的工作者线程会被唤醒。
 *
 **/
public class DefaultThreadPool<Job extends Runnable> implements ThreadPoolService<Job> {

    // 线程池最大限制数量
    private static final int MAX_WORKER_NUMBERS = 10;
    // 线程池默认数量
    private static final int DEFAULT_WORKER_NUMBERS = 5;
    // 线程池最小数量
    private static final int MIN_WORKER_NUMBERS = 1;
    // 工作列表，用于添加工作
    private final LinkedList<Job> jobs = new LinkedList<>();
    // 工作者列表
    private final List<Worker> workers = Collections.synchronizedList(new ArrayList<>());
    // 工作者线程数
    private int workerNum = DEFAULT_WORKER_NUMBERS;
    // 生成线程编号
    private AtomicLong threadNum = new AtomicLong();

    public DefaultThreadPool() {
        initializeWorkers(DEFAULT_WORKER_NUMBERS);
    }

    public DefaultThreadPool(int num) {
        this.workerNum = num > MAX_WORKER_NUMBERS ? MAX_WORKER_NUMBERS :
                num < MIN_WORKER_NUMBERS ? MIN_WORKER_NUMBERS : num;

        initializeWorkers(this.workerNum);
    }

    /**
     * 初始化线程工作者
     * @param num
     */
    private void initializeWorkers(int num) {
        for (int i = 0; i < num; i++) {
            Worker worker = new Worker();
            workers.add(worker);
            Thread thread = new Thread(worker, "ThreadPool-Worker-" + threadNum.incrementAndGet());
            thread.start();
        }
    }

    @Override
    public void execute(Job job) {
        if (job != null) {
            // 添加工作并通知
            synchronized (jobs) {
                jobs.addLast(job);
                jobs.notify();
            }
        }
    }

    @Override
    public void sutdown() {
        workers.forEach(Worker::shutdown);
    }

    @Override
    public void addWorkers(int num) {
        synchronized (jobs) {
            if (num + this.workerNum > MAX_WORKER_NUMBERS) {
                num = MAX_WORKER_NUMBERS - this.workerNum;
            }
            initializeWorkers(num);
            this.workerNum += num;
        }
    }

    @Override
    public void removeWorkers(int num) {
        synchronized (jobs) {
            if (num >= this.workerNum) {
                throw new IllegalArgumentException("beyond workNum");
            }
            int count = 0;
            while (count < num) {
                Worker worker = workers.get(count);
                if (workers.remove(worker)) {
                    worker.shutdown();
                    count++;
                }
            }
            this.workerNum -= count;
        }
    }

    @Override
    public int getJobSize() {
        return jobs.size();
    }

    /**
     * 工作者，负责消费任务
     */
    class Worker implements Runnable {
        // 是否在运行工作
        private volatile boolean running = true;

        @Override
        public void run() {
            while (running) {
                Job job = null;
                synchronized (jobs) {
                    // 工作列表为空，就wait
                    while (jobs.isEmpty()) {
                        try {
                            jobs.wait();
                        } catch (InterruptedException e) {
                            // 感知外部对WorkerThread的中断操作，返回。
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    // 取出一个任务
                    job = jobs.removeFirst();
                }

                if (job != null) {
                    try {
                        job.run();
                    } catch (Exception e) {
                        // 忽略Job执行中的异常
                    }
                }
            }
        }

        public void shutdown() {
            running = false;
        }
    }

}
