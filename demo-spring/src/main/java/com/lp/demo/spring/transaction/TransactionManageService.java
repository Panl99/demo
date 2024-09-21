package com.lp.demo.spring.transaction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * TransactionSynchronization 工具类 TODO 未验证
 */
@Slf4j
public class TransactionManageService {

    private static ExecutorService defaultExecutorService = Executors.newCachedThreadPool();

    public static <T> void afterCommitDefault(T args, Consumer<? super T> consumer) {
        afterCommit(args, consumer, defaultExecutorService);
    }

    public static void setDefaultExecutorService(ExecutorService executorService) {
        defaultExecutorService = executorService;
    }

    /**
     * 添加钩子来跟踪异步操作的性能。
     *
     * @param args
     * @param consumer
     * @param executorService
     * @param <T>
     */
    public static <T> void afterCommit(T args, Consumer<? super T> consumer, ExecutorService executorService) {
        Objects.requireNonNull(consumer, "Consumer must not be null");
        Objects.requireNonNull(executorService, "ExecutorService must not be null");
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                executorService.submit(() -> {
                    long startTime = System.currentTimeMillis();
                    try {
                        consumer.accept(args);
                    } catch (Exception e) {
                        log.error("Error executing afterCommit", e);
                    } finally {
                        long duration = System.currentTimeMillis() - startTime;
                        log.info("afterCommit executed in {} ms", duration);
                    }
                });
            }
        });
    }

    /**
     * @param args
     * @param consumer
     * @param executorService
     * @param errorMsg
     * @param <T>
     */
    public static <T> void afterCommit(T args, Consumer<? super T> consumer, ExecutorService executorService, String errorMsg) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                executorService.submit(() -> {
                    try {
                        consumer.accept(args);
                    } catch (Exception e) {
                        log.error(errorMsg, e);
                    }
                });
            }
        });
    }

    /**
     * @param args
     * @param consumer
     * @param <T>
     */
    public static <T> void afterCommit(T args, Consumer<? super T> consumer) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                consumer.accept(args);
            }
        });
    }

    /**
     * @param runnable
     * @param executorService
     * @param <T>
     */
    public static <T> void afterCommit(Runnable runnable, ExecutorService executorService) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                executorService.submit(() -> {
                    try {
                        runnable.run();
                    } catch (Exception e) {
                        log.error("run fail", e);
                    }
                });
            }
        });
    }

    /**
     * 增加事务回滚后的操作支持
     *
     * @param args
     * @param consumer
     * @param <T>
     */
    public static <T> void afterRollback(T args, Consumer<? super T> consumer) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                if (status == TransactionSynchronization.STATUS_ROLLED_BACK) {
                    consumer.accept(args);
                }
            }
        });
    }

    // 在事务提交后执行给定的操作
    public static void afterCommit(Runnable action) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                action.run();
            }
        });
    }

    // 在事务回滚后执行给定的操作
    public static void afterRollback(Runnable action) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                if (status == TransactionSynchronization.STATUS_ROLLED_BACK) {
                    action.run();
                }
            }
        });
    }

    // 在事务提交后执行给定的操作，并消费事务结果
    public static <T> void afterCommit(Consumer<T> action, T result) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                action.accept(result);
            }
        });
    }

    // 在事务回滚后执行给定的操作，并消费事务结果
    public static <T> void afterRollback(Consumer<T> action, T result) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                if (status == TransactionSynchronization.STATUS_ROLLED_BACK) {
                    action.accept(result);
                }
            }
        });
    }

}