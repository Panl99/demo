package com.lp.demo.redis.redisson.delayqueue;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 延迟队列消费者管理器
 * 统一管理所有延迟队列消费者，支持优雅关闭
 * 新增队列只需：
 * 1. 在 DeviceDelayedQueueStrategyEnum 中添加枚举值
 * 2. 实现 RedisDelayedQueueListener 接口并注册到 StrategyFactory
 */
@Slf4j
@Component
public class DelayedQueueConsumerManager implements SmartLifecycle {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private StrategyFactory<DelayedQueueStrategyEnum, RedisDelayedQueueListener> strategyFactory;

    /**
     * 消费者线程
     */
    private final Map<String, Thread> consumerThreads = new ConcurrentHashMap<>();

    /**
     * 延迟队列引用，防止GC回收导致延时失效
     */
    private final Map<String, RDelayedQueue<?>> delayedQueues = new ConcurrentHashMap<>();

    /**
     * 防止 stop() 与 stop(Runnable) 并发执行 stopAllConsumers
     */
    private final AtomicBoolean stopping = new AtomicBoolean(false);

    /**
     * 防止 start() 并发重复启动
     */
    private final AtomicBoolean starting = new AtomicBoolean(false);

    private volatile boolean running = false;

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void start() {
        // 重置停止标志，支持 Spring Context 刷新后重新启动
        stopping.set(false);
        // 防止并发重复启动
        if (!starting.compareAndSet(false, true)) {
            log.warn("延迟队列消费者管理器正在启动中，跳过");
            return;
        }
        try {
            running = true;
            startAllConsumers();
        } finally {
            starting.set(false);
        }
    }

    /**
     * 异步优雅停机（Spring SmartLifecycle 优先调用此方法）
     */
    @Override
    public void stop(Runnable callback) {
        running = false;
        Thread stopThread = new Thread(() -> {
            try {
                stopAllConsumers();
            } catch (Exception e) {
                log.error("停止延迟队列消费者异常", e);
            } finally {
                callback.run();
            }
        }, "delayed-queue-manager-stop");
        // 非守护线程，确保关闭逻辑尽量执行完
        stopThread.setDaemon(false);
        stopThread.start();
    }

    /**
     * 同步停机（供外部直接调用，如测试/运维手动触发）
     * Spring 默认只会调用 stop(Runnable)，此方法用于编程式直接停止
     * 与 stop(Runnable) 共享同一停止逻辑，通过 AtomicBoolean 保证只执行一次
     */
    @Override
    public void stop() {
        running = false;
        stopAllConsumers();
        log.info("延迟队列消费者管理器停止完成");
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    /**
     * 启动所有消费者
     * 遍历 DelayedQueueStrategyEnum 枚举精确获取延迟队列策略，
     * 避免 list() 获取到其他类型的策略（如 NoticeStrategyService）
     */
    private void startAllConsumers() {
        EnumSet<DelayedQueueStrategyEnum> strategies = EnumSet.allOf(DelayedQueueStrategyEnum.class);
        log.info("发现 {} 个延迟队列策略枚举", strategies.size());

        for (DelayedQueueStrategyEnum strategy : strategies) {
            RedisDelayedQueueListener listener = strategyFactory.get(strategy);

            if (listener == null) {
                log.warn("延迟队列策略 [{}] 未注册对应的监听器，跳过", strategy);
                continue;
            }

            String queueName = strategy.getQueueName();

            // 检查现有线程状态
            Thread existThread = consumerThreads.get(queueName);
            if (existThread != null) {
                if (existThread.isAlive()) {
                    log.warn("延迟队列 [{}] 消费者已存在且存活，跳过", queueName);
                    continue;
                } else {
                    // 已死亡的线程，先移除
                    log.warn("延迟队列 [{}] 消费者线程已死亡，将重新启动", queueName);
                    consumerThreads.remove(queueName, existThread);
                }
            }

            startConsumer(queueName, listener);
        }
        log.info("延迟队列消费者管理器启动完成，共启动 {} 个消费者", consumerThreads.size());
    }

    /**
     * 启动单个消费者（保留泛型支持，避免ClassCastException）
     * 先原子性放入Map，再启动线程，防止竞态导致双消费
     */
    private <T> void startConsumer(String queueName, RedisDelayedQueueListener<T> listener) {
        RBlockingQueue<T> blockingQueue = redissonClient.getBlockingQueue(queueName);
        RDelayedQueue<T> delayedQueue = redissonClient.getDelayedQueue(blockingQueue);
        delayedQueues.put(queueName, delayedQueue);

        Thread thread = new Thread(() -> {
            log.info("启动延迟队列 [{}] 消费者线程", queueName);
            while (running && !Thread.currentThread().isInterrupted()) {
                try {
                    T message = blockingQueue.poll(5, TimeUnit.SECONDS);
                    if (message != null) {
                        listener.invoke(message);
                    }
                } catch (InterruptedException e) {
                    log.warn("延迟队列 [{}] 消费者线程被中断", queueName);
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    log.error("延迟队列 [{}] 消费异常", queueName, e);
                }
            }
            log.info("延迟队列 [{}] 消费者线程退出", queueName);
        }, "delayed-queue-consumer-" + queueName);
        thread.setDaemon(true);

        // 原子性放入Map：先占位，后启动。若已有则丢弃当前线程（不启动）
        Thread previous = consumerThreads.putIfAbsent(queueName, thread);
        if (previous != null) {
            log.warn("延迟队列 [{}] 已存在消费者线程，跳过启动", queueName);
            // 回滚已放入的延迟队列引用
            delayedQueues.remove(queueName);
            return;
        }

        // 放入成功，再启动线程
        thread.start();
    }

    /**
     * 停止所有消费者
     * 通过 AtomicBoolean 保证全局只执行一次，防止 stop() 与 stop(Runnable) 并发
     * 超时未死的线程保留引用，防止幽灵线程导致重复消费
     * 仅清理已成功停止的 delayedQueues 引用，超时线程的引用保留防止GC
     */
    private void stopAllConsumers() {
        if (!stopping.compareAndSet(false, true)) {
            log.info("延迟队列消费者管理器已在停止中，跳过");
            return;
        }

        log.info("开始停止所有延迟队列消费者，共 {} 个", consumerThreads.size());
        int stoppedCount = 0;
        int timeoutCount = 0;
        for (Map.Entry<String, Thread> entry : consumerThreads.entrySet()) {
            String queueName = entry.getKey();
            Thread thread = entry.getValue();
            if (thread.isAlive()) {
                thread.interrupt();
                try {
                    // 最多等待5秒让消费者线程处理完当前消息
                    thread.join(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                if (thread.isAlive()) {
                    // 超时仍未死亡：保留引用防止重复启动，保留 delayedQueue 引用防止GC
                    timeoutCount++;
                    log.error("延迟队列 [{}] 消费者线程停止超时，业务可能卡死！需人工介入排查或重启JVM，否则该队列将永久无法消费", queueName);
                } else {
                    // 已成功终止，移除线程引用和队列引用
                    stoppedCount++;
                    log.info("延迟队列 [{}] 消费者线程已停止", queueName);
                    consumerThreads.remove(queueName, thread);
                    delayedQueues.remove(queueName);
                }
            }
        }
        log.info("延迟队列消费者停止完成：成功停止 {} 个，超时残留 {} 个{}", stoppedCount, timeoutCount, timeoutCount > 0 ? "（残留线程需重启JVM才能恢复）" : "");
    }
}