package com.lp.demo.action.java_in_action;

import com.lp.demo.common.util.ConsoleColorUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lp
 * @date 2022/11/17 13:52
 * @desc 同步屏障CyclicBarrier
 **/
public class CyclicBarrierDemo {

//    static CyclicBarrier c = new CyclicBarrier(2);


    public static void main(String[] args) {
        ConsoleColorUtil.printDefaultColor("=================cyclicBarrierTest===================");
        simpleCyclicBarrierTest(2); // 4 1 5 2 3 或 1 4 5 2 3 因为主线程和子线程的调度是由CPU决定的，两个线程都有可能先执行

//        simpleCyclicBarrierTest(3); // 4 1 或 1 4 之后阻塞住，// 这里是3，主线程和子线程会一直阻塞，因为没有第三个线程执行await()方法，即没有第三个线程到达屏障，前两个到达屏障的线程都不会执行。

        ConsoleColorUtil.printDefaultColor("=================cyclicBarrierTest2===================");
        simpleCyclicBarrierTest2(2); // 55 11 77 22 66 33 44 // 因为CyclicBarrier设置了拦截线程的数量是2，所以必须等代码中的第一个线程和线程A都执行完之后，才会继续执行主线程
    }

    public static void simpleCyclicBarrierTest(int parties) {
        CyclicBarrier c = new CyclicBarrier(parties);
        // 子线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ConsoleColorUtil.printDefaultColor("1");
                    Thread.sleep(1000);
                    c.await();
                    Thread.sleep(2000);
                    ConsoleColorUtil.printDefaultColor("2");
                } catch (Exception e) {
                }
                ConsoleColorUtil.printDefaultColor("3");
            }
        }).start();

        // 主线程
        try {
            ConsoleColorUtil.printDefaultColor("4");
            c.await();
        } catch (Exception e) {
        }
        ConsoleColorUtil.printDefaultColor("5");
    }


    public static void simpleCyclicBarrierTest2(int parties) {
        CyclicBarrier c = new CyclicBarrier(parties, new A());
        // 子线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ConsoleColorUtil.printDefaultColor("11");
                    Thread.sleep(1000);
                    c.await();
                    ConsoleColorUtil.printDefaultColor("22");
                    Thread.sleep(2000);
                    ConsoleColorUtil.printDefaultColor("33");
                } catch (Exception e) {
                }
                ConsoleColorUtil.printDefaultColor("44");
            }
        }).start();

        // 主线程
        try {
            ConsoleColorUtil.printDefaultColor("55");
            c.await();
        } catch (Exception e) {
        }
        ConsoleColorUtil.printDefaultColor("66");
    }

    static class A implements Runnable {
        @Override
        public void run() {
            ConsoleColorUtil.printDefaultColor("77");
        }
    }


    /**
     * 银行流水处理服务类
     *
     * 例如，用一个Excel保存了用户所有银行流水，每个Sheet保存一个账户近一年的每笔银行流水，现在需要统计用户的日均银行流水，
     * 先用 多线程处理每个sheet里的银行流水，都执行完之后，得到每个sheet的日均银行流水，
     * 最后 再用barrierAction用这些线程的计算结果，计算出整个Excel的日均银行流水
     *
     */
    static class BankWaterService implements Runnable {
        /**
         * 创建4个屏障，处理完之后执行当前类的run方法
         */
        private final CyclicBarrier c = new CyclicBarrier(4, this);
        /**
         * 假设只有4个sheet，所以只启动4个线程
         */
        private final Executor executor = Executors.newFixedThreadPool(4);
        /**
         * 保存每个sheet计算出的银流结果
         */
        private final ConcurrentHashMap<String, Integer> sheetBankWaterCount = new ConcurrentHashMap<>();

        private void count() {
            for (int i = 0; i < 4; i++) {
                ConsoleColorUtil.printDefaultColor("index = " + i);
                executor.execute(() -> {
                    // 计算当前sheet的银流数据，计算代码省略，这里用1表示计算结果
                    sheetBankWaterCount.put(Thread.currentThread().getName(), 1);
                    // 银流计算完成，插入一个屏障
                    try {
                        Thread.sleep(1000);
                        ConsoleColorUtil.printDefaultColor("111 - ");
                        c.await();
                        ConsoleColorUtil.printDefaultColor("222 - ");
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                });
            }
        }

        @Override
        public void run() {
            int result = 0;
            // 汇总每个sheet计算出的结果
            for (Map.Entry<String, Integer> sheet : sheetBankWaterCount.entrySet()) {
                result += sheet.getValue();
            }
            // 将结果输出
            sheetBankWaterCount.put("result", result);
            ConsoleColorUtil.printDefaultColor("sheetBankWaterCount = " + sheetBankWaterCount);
        }

        public static void main(String[] args) {
            BankWaterService bankWaterCount = new BankWaterService();
            bankWaterCount.count();
        }
    }


    /**
     * isBroken()判断阻塞线程是否被中断
     */
    static class CyclicBarrierTest3 {
        static CyclicBarrier c = new CyclicBarrier(2);

        public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        c.await();
                    } catch (Exception e) {
                    }
                }
            });
            thread.start();
            thread.interrupt();
            try {
                c.await();
            } catch (Exception e) {
                // 阻塞线程是否被中断
                System.out.println(c.isBroken());
            }
        }
    }

    /**
     * 统计数据接口
     * 多线程查询数据 合并返回
     *
     * @param <T> 返回数据对象类型
     * @return
     */
    // @Override
    public <T> List<T> getStatisticsData() {
        WorkerThread<T> workerThread = new WorkerThread<>();
        return workerThread.getData();
    }

    class WorkerThread<T> {
        private static final int THREAD_COUNT = 2;
        private final List<T> dataOverview = new ArrayList<>();
        private final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        private final CyclicBarrier barrier = new CyclicBarrier(THREAD_COUNT + 1);

        private List<T> getData() {
            for (int i = 0; i < THREAD_COUNT; i++) {
                int tid = i;
                executorService.execute(() -> {
                    switch (tid) {
                        case 0:
                            // 查询数据A
                            dataOverview.add(getA());
                            break;
                        case 1:
                            // 查询数据B
                            dataOverview.add(getB());
                            break;
                        default:
                            return;
                    }

                    try {
                        barrier.await();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        System.out.println("worker sub thread exception, e: " + e);
                        barrier.reset();
                    }
                });
            }
            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                System.out.println("worker main thread exception, e: " + e);
                barrier.reset();
            }
            executorService.shutdown();
            return dataOverview;
        }

        // 具体查询方法
        private T getA() {
            return null;
        }
        private T getB() {
            return null;
        }
    }

}
