package com.lp.demo.action.java_in_action.CompletableFuture;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * CompletableFuture 异步非阻塞工具类（Java 8 兼容）。
 * <p>
 * 全项目异步与并行执行的统一入口。
 * <p>
 * 能力分组：
 * <ol>
 *     <li>基础异步：{@link #supplyAsync} / {@link #runAsync} / {@link #failed}</li>
 *     <li>超时控制：{@link #orTimeout} / {@link #orDefaultOnTimeout} / {@link #orFallbackOnTimeout}</li>
 *     <li>异常兜底与解包：{@link #unwrap} / {@link #fallback} / {@link #fallbackIfNull}</li>
 *     <li>失败重试：{@link #retry}，支持指数退避</li>
 *     <li>聚合收敛：{@link #allOf} / {@link #allOfSafely} / {@link #allOfMap} / {@link #anyOf} / {@link #anyOfSuccess}</li>
 *     <li>批量并行执行：{@link #execute} / {@link #allOfList}</li>
 *     <li>回调监听：{@link #toCompletableFuture} / {@link #onComplete}</li>
 * </ol>
 * <p>
 * 线程池设计说明：
 * <ol>
 *     <li>核心线程数与最大线程数一致：避免有界队列积压时线程池不扩容的缺陷；</li>
 *     <li>有界队列：防止无界堆积导致内存溢出；</li>
 *     <li>CallerRunsPolicy：队列满时由提交线程自己执行，起到限流降级作用，不丢任务也不抛拒绝异常；</li>
 *     <li>核心线程允许超时回收：低峰期自动释放线程资源。</li>
 * </ol>
 */
@Slf4j
public class AsyncFutureUtil {

    private AsyncFutureUtil() {
    }

    // ==================== 线程资源 ====================

    /**
     * 默认线程池核心线程数
     */
    private static final int DEFAULT_CORE_SIZE = Math.max(4, Runtime.getRuntime().availableProcessors() * 2);
    /**
     * 默认线程池队列容量，配合 CallerRunsPolicy 形成背压
     */
    private static final int DEFAULT_QUEUE_CAPACITY = 512;

    /**
     * 默认线程池，懒加载单例
     */
    private static final class DefaultExecutorHolder {
        private static final ThreadPoolExecutor INSTANCE;

        static {
            INSTANCE = new ThreadPoolExecutor(
                    DEFAULT_CORE_SIZE, DEFAULT_CORE_SIZE,
                    60L, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(DEFAULT_QUEUE_CAPACITY),
                    new NamedThreadFactory("async-future-", false),
                    new ThreadPoolExecutor.CallerRunsPolicy());
            INSTANCE.allowCoreThreadTimeOut(true);
        }
    }

    /**
     * 定时调度器：只承担超时触发与重试退避，不执行任何业务逻辑
     */
    private static final ScheduledExecutorService SCHEDULER;

    static {
        ScheduledThreadPoolExecutor scheduler =
                new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("async-future-timer-", true));
        // 任务取消后从队列移除，避免长队列内存堆积
        scheduler.setRemoveOnCancelPolicy(true);
        SCHEDULER = scheduler;
    }

    private static volatile Executor defaultExecutor = DefaultExecutorHolder.INSTANCE;

    /**
     * 获取默认线程池
     */
    public static Executor getDefaultExecutor() {
        return defaultExecutor;
    }

    /**
     * 替换默认线程池，传 null 表示恢复内置线程池。
     * 典型用法：Spring 容器启动后注入业务自定义的 ThreadPoolTaskExecutor。
     */
    public static void setDefaultExecutor(Executor executor) {
        defaultExecutor = executor == null ? DefaultExecutorHolder.INSTANCE : executor;
    }

    private static Executor resolve(Executor executor) {
        return executor == null ? defaultExecutor : executor;
    }

    // ==================== 1. 基础异步 ====================

    /**
     * 提交有返回值的异步任务，使用默认线程池
     */
    public static <T> CompletableFuture<T> supplyAsync(Supplier<T> supplier) {
        return supplyAsync(supplier, defaultExecutor);
    }

    /**
     * 提交有返回值的异步任务，使用指定线程池
     */
    public static <T> CompletableFuture<T> supplyAsync(Supplier<T> supplier, Executor executor) {
        Objects.requireNonNull(supplier, "supplier");
        return CompletableFuture.supplyAsync(supplier, resolve(executor));
    }

    /**
     * 提交无返回值的异步任务，使用默认线程池
     */
    public static CompletableFuture<Void> runAsync(Runnable runnable) {
        return runAsync(runnable, defaultExecutor);
    }

    /**
     * 提交无返回值的异步任务，使用指定线程池
     */
    public static CompletableFuture<Void> runAsync(Runnable runnable, Executor executor) {
        Objects.requireNonNull(runnable, "runnable");
        return CompletableFuture.runAsync(runnable, resolve(executor));
    }

    /**
     * 构造一个已失败的 CompletableFuture（Java 8 无 failedFuture）
     */
    public static <T> CompletableFuture<T> failed(Throwable throwable) {
        CompletableFuture<T> future = new CompletableFuture<>();
        future.completeExceptionally(throwable);
        return future;
    }

    // ==================== 2. 超时控制 ====================

    /**
     * 超时后以 {@link TimeoutException} 结束。
     * <p>
     * 返回的是新的 CompletableFuture，入参 future 的状态不被污染。
     */
    public static <T> CompletableFuture<T> orTimeout(CompletableFuture<T> future, long timeout, TimeUnit unit) {
        Objects.requireNonNull(future, "future");
        CompletableFuture<T> result = new CompletableFuture<>();
        ScheduledFuture<?> timer = SCHEDULER.schedule(
                () -> result.completeExceptionally(
                        new TimeoutException("async task timeout after " + timeout + " " + unit)),
                timeout, unit);
        future.whenComplete((value, throwable) -> {
            timer.cancel(false);
            if (throwable == null) {
                result.complete(value);
            } else {
                result.completeExceptionally(unwrap(throwable));
            }
        });
        return result;
    }

    /**
     * 超时返回默认值；非超时的业务异常仍然向上抛出，不做吞没
     */
    public static <T> CompletableFuture<T> orDefaultOnTimeout(CompletableFuture<T> future, long timeout,
                                                              TimeUnit unit, T defaultValue) {
        return orFallbackOnTimeout(future, timeout, unit, () -> defaultValue);
    }

    /**
     * 超时走兜底函数；非超时的业务异常仍然向上抛出，不做吞没
     */
    public static <T> CompletableFuture<T> orFallbackOnTimeout(CompletableFuture<T> future, long timeout,
                                                               TimeUnit unit, Supplier<T> fallback) {
        Objects.requireNonNull(fallback, "fallback");
        return orTimeout(future, timeout, unit).exceptionally(throwable -> {
            Throwable real = unwrap(throwable);
            if (!isTimeout(real)) {
                throw new CompletionException(real);
            }
            log.warn("async task timeout, fallback used, error:{}", real.getMessage());
            return fallback.get();
        });
    }

    // ==================== 3. 异常兜底与解包 ====================

    /**
     * 解包 CompletableFuture 包装后的真实异常（剥掉 CompletionException / ExecutionException 外壳）
     */
    public static Throwable unwrap(Throwable throwable) {
        if (throwable instanceof CompletionException || throwable instanceof ExecutionException) {
            if (throwable.getCause() != null) {
                return throwable.getCause();
            }
        }
        return throwable;
    }

    /**
     * 异常时返回默认值
     */
    public static <T> CompletableFuture<T> fallback(CompletableFuture<T> future, T defaultValue) {
        Objects.requireNonNull(future, "future");
        return future.exceptionally(throwable -> {
            Throwable real = unwrap(throwable);
            log.warn("async task failed, fallback used, error:{}", real.getMessage());
            return defaultValue;
        });
    }

    /**
     * 异常时或结果为空时返回默认值
     */
    public static <T> CompletableFuture<T> fallbackIfNull(CompletableFuture<T> future, T defaultValue) {
        return fallback(future, defaultValue).thenApply(value -> value == null ? defaultValue : value);
    }

    // ==================== 4. 失败重试 ====================

    /**
     * 失败重试，默认线程池、无退避
     *
     * @param maxRetries 最大重试次数（不含首次执行），0 表示不重试
     */
    public static <T> CompletableFuture<T> retry(Supplier<T> supplier, int maxRetries) {
        return retry(supplier, maxRetries, 0, TimeUnit.MILLISECONDS, defaultExecutor);
    }

    /**
     * 失败重试，默认线程池、按 delay 做指数退避
     */
    public static <T> CompletableFuture<T> retry(Supplier<T> supplier, int maxRetries, long delay, TimeUnit unit) {
        return retry(supplier, maxRetries, delay, unit, defaultExecutor);
    }

    /**
     * 失败重试，指定线程池、按 delay 做指数退避（delay * 2^(attempt-1)，最多放大 32 倍）
     */
    public static <T> CompletableFuture<T> retry(Supplier<T> supplier, int maxRetries, long delay,
                                                 TimeUnit unit, Executor executor) {
        Objects.requireNonNull(supplier, "supplier");
        Objects.requireNonNull(unit, "unit");
        return retryInternal(supplier, Math.max(maxRetries, 0), 1, unit.toNanos(delay), resolve(executor));
    }

    private static <T> CompletableFuture<T> retryInternal(Supplier<T> supplier, int maxRetries, int attempt,
                                                          long baseDelayNanos, Executor executor) {
        return supplyAsync(supplier, executor).handle((value, throwable) -> {
            if (throwable == null) {
                return CompletableFuture.completedFuture(value);
            }
            Throwable real = unwrap(throwable);
            if (attempt > maxRetries) {
                log.error("async task retry exhausted, attempts:{}, error:{}", attempt, real.getMessage());
                return AsyncFutureUtil.<T>failed(real);
            }
            long backoffNanos = backoffNanos(baseDelayNanos, attempt);
            log.warn("async task failed, retry {}/{}, backoff {}ms, error:{}",
                    attempt, maxRetries, backoffNanos / 1_000_000, real.getMessage());
            return delayAfter(backoffNanos).thenCompose(v -> retryInternal(supplier, maxRetries, attempt + 1,
                    baseDelayNanos, executor));
        }).thenCompose(future -> future);
    }

    private static long backoffNanos(long baseDelayNanos, int attempt) {
        if (baseDelayNanos <= 0) {
            return 0L;
        }
        return baseDelayNanos * (1L << Math.min(attempt - 1, 5));
    }

    private static CompletableFuture<Void> delayAfter(long delayNanos) {
        if (delayNanos <= 0) {
            return CompletableFuture.completedFuture(null);
        }
        CompletableFuture<Void> delay = new CompletableFuture<>();
        SCHEDULER.schedule(() -> delay.complete(null), delayNanos, TimeUnit.NANOSECONDS);
        return delay;
    }

    // ==================== 5. 聚合收敛与批量执行 ====================

    /**
     * 全部成功才成功，结果顺序与入参顺序一致；任一失败则整体失败
     */
    public static <T> CompletableFuture<List<T>> allOf(Collection<CompletableFuture<T>> futures) {
        Objects.requireNonNull(futures, "futures");
        List<CompletableFuture<T>> list = new ArrayList<>(futures);
        return CompletableFuture.allOf(list.toArray(new CompletableFuture<?>[0]))
                .thenApply(v -> list.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()));
    }

    /**
     * 单个任务失败降级为默认值，保证"部分失败不整体失败"，结果顺序与入参顺序一致
     */
    public static <T> CompletableFuture<List<T>> allOfSafely(Collection<CompletableFuture<T>> futures,
                                                             T defaultValue) {
        Objects.requireNonNull(futures, "futures");
        List<CompletableFuture<T>> safeFutures = futures.stream()
                .map(future -> future.exceptionally(throwable -> {
                    Throwable real = unwrap(throwable);
                    log.warn("async task failed, degrade to default value, error:{}", real.getMessage());
                    return defaultValue;
                }))
                .collect(Collectors.toList());
        return CompletableFuture.allOf(safeFutures.toArray(new CompletableFuture<?>[0]))
                .thenApply(v -> safeFutures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()));
    }

    /**
     * 多个 Map 结果合并为一个 Map，key 冲突时后写入的覆盖先写入的
     */
    public static <K, V> CompletableFuture<Map<K, V>> allOfMap(
            Collection<CompletableFuture<Map<K, V>>> futures) {
        Objects.requireNonNull(futures, "futures");
        List<CompletableFuture<Map<K, V>>> list = new ArrayList<>(futures);
        return CompletableFuture.allOf(list.toArray(new CompletableFuture<?>[0]))
                .thenApply(v -> list.stream()
                        .map(CompletableFuture::join)
                        .flatMap(map -> map.entrySet().stream())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b)));
    }

    /**
     * 竞速：任意一个任务完成（无论成功或失败）即返回
     */
    @SuppressWarnings("unchecked")
    public static <T> CompletableFuture<T> anyOf(Collection<CompletableFuture<T>> futures) {
        Objects.requireNonNull(futures, "futures");
        return (CompletableFuture<T>) CompletableFuture.anyOf(futures.toArray(new CompletableFuture<?>[0]));
    }

    /**
     * 竞速：取第一个"成功"的结果，全部失败时以最后一个异常结束。
     * 入参为空时以 {@link IllegalArgumentException} 结束。
     */
    public static <T> CompletableFuture<T> anyOfSuccess(Collection<CompletableFuture<T>> futures) {
        Objects.requireNonNull(futures, "futures");
        if (futures.isEmpty()) {
            return failed(new IllegalArgumentException("futures is empty"));
        }
        CompletableFuture<T> result = new CompletableFuture<>();
        AtomicInteger remaining = new AtomicInteger(futures.size());
        AtomicReference<Throwable> lastError = new AtomicReference<>();
        for (CompletableFuture<T> future : futures) {
            future.whenComplete((value, throwable) -> {
                if (throwable == null) {
                    result.complete(value);
                    return;
                }
                lastError.set(unwrap(throwable));
                if (remaining.decrementAndGet() == 0) {
                    log.warn("all async tasks failed, error:{}", lastError.get().getMessage());
                    result.completeExceptionally(lastError.get());
                }
            });
        }
        return result;
    }

    /**
     * 批量提交任务并按入参顺序收集结果
     */
    public static <T> CompletableFuture<List<T>> allOfList(List<Supplier<T>> suppliers, Executor executor) {
        Objects.requireNonNull(suppliers, "suppliers");
        List<CompletableFuture<T>> futures = new ArrayList<>(suppliers.size());
        for (Supplier<T> supplier : suppliers) {
            futures.add(supplyAsync(supplier, executor));
        }
        return allOf(futures);
    }

    /**
     * 批量并行执行任务，按入参维度收集结果（入参自动去重、忽略null），使用默认线程池。
     * <p>
     * 该方法为阻塞式（内部 {@code join}），线程池并行度敏感的场景请使用
     * {@link #execute(Collection, Function, Executor)} 显式传入业务自管的线程池。
     *
     * @param items 入参集合
     * @param task  单个入参对应的任务；返回null时结果不收集；
     *              任务抛出的异常在聚合时以CompletionException抛出，需要容错的场景请在task内部自行捕获处理
     * @param <T>   入参类型
     * @param <R>   结果类型
     * @return 入参 -&gt; 任务结果，入参为空集合时返回空Map
     */
    public static <T, R> Map<T, R> execute(Collection<T> items, Function<T, R> task) {
        return execute(items, task, defaultExecutor);
    }

    /**
     * 批量并行执行任务，按入参维度收集结果（入参自动去重、忽略null），使用指定线程池
     *
     * @param items    入参集合
     * @param task     单个入参对应的任务；返回null时结果不收集；
     *                 任务抛出的异常在聚合时以CompletionException抛出，需要容错的场景请在task内部自行捕获处理
     * @param executor 业务自管的线程池
     * @param <T>      入参类型
     * @param <R>      结果类型
     * @return 入参 -&gt; 任务结果，入参为空集合时返回空Map
     */
    public static <T, R> Map<T, R> execute(Collection<T> items, Function<T, R> task, Executor executor) {
        if (items == null || items.isEmpty() || task == null) {
            return Collections.emptyMap();
        }
        Map<T, R> result = new ConcurrentHashMap<>();
        CompletableFuture<?>[] futures = items.stream()
                .filter(Objects::nonNull)
                .distinct()
                .map(item -> CompletableFuture.runAsync(() -> {
                    R r = task.apply(item);
                    if (r != null) {
                        result.put(item, r);
                    }
                }, resolve(executor)))
                .toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(futures).join();
        return result;
    }

    // ==================== 6. 回调监听 ====================

    /**
     * 异步回调结果处理器
     */
    public interface Handler<T> {
        void onSuccess(T result);

        void onFailure(Throwable throwable);
    }

    /**
     * 注册式异步回调，由调用方实现（如 http / mq / rpc 的 callback）
     */
    public interface Callback<T> {
        void register(Handler<T> handler);
    }

    /**
     * 触发一次异步调用
     */
    @FunctionalInterface
    public interface AsyncCall {
        void invoke() throws Exception;
    }

    /**
     * 回调式异步 API 转 CompletableFuture，之后即可使用 CF 的任意能力
     *
     * @param callback  负责注册观察者
     * @param asyncCall 负责发起调用，可为 null（表示由 callback 自身触发）
     */
    public static <T> CompletableFuture<T> toCompletableFuture(Callback<T> callback, AsyncCall asyncCall) {
        Objects.requireNonNull(callback, "callback");
        CompletableFuture<T> future = new CompletableFuture<>();
        callback.register(new Handler<T>() {
            @Override
            public void onSuccess(T result) {
                future.complete(result);
            }

            @Override
            public void onFailure(Throwable throwable) {
                future.completeExceptionally(throwable);
            }
        });
        if (asyncCall != null) {
            try {
                asyncCall.invoke();
            } catch (Throwable throwable) {
                future.completeExceptionally(throwable);
            }
        }
        return future;
    }

    /**
     * 在已有的 CompletableFuture 上挂载 success/failure 双回调，不改变原有结果
     */
    public static <T> CompletableFuture<T> onComplete(CompletableFuture<T> future, Consumer<T> onSuccess,
                                                      Consumer<Throwable> onFailure) {
        Objects.requireNonNull(future, "future");
        return future.whenComplete((value, throwable) -> {
            if (throwable == null) {
                if (onSuccess != null) {
                    onSuccess.accept(value);
                }
            } else if (onFailure != null) {
                onFailure.accept(unwrap(throwable));
            }
        });
    }

    // ==================== 工具 ====================

    private static boolean isTimeout(Throwable throwable) {
        Throwable current = throwable;
        while (current != null) {
            if (current instanceof TimeoutException) {
                return true;
            }
            if (current == current.getCause()) {
                return false;
            }
            current = current.getCause();
        }
        return false;
    }

    private static class NamedThreadFactory implements ThreadFactory {
        private final AtomicInteger counter = new AtomicInteger(1);
        private final String prefix;
        private final boolean daemon;

        NamedThreadFactory(String prefix, boolean daemon) {
            this.prefix = prefix;
            this.daemon = daemon;
        }

        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable, prefix + counter.getAndIncrement());
            thread.setDaemon(daemon);
            return thread;
        }
    }

    // ==================== 自测入口 ====================

    public static void main(String[] args) throws Exception {
        // 1. 基础异步 + 自定义线程池
        System.out.println("[1] supplyAsync = " + supplyAsync(() -> "hello").thenApply(String::toUpperCase).get());
        runAsync(() -> System.out.println("[1] runAsync on " + Thread.currentThread().getName())).get();

        // 2. 超时控制：超时给默认值，非超时异常照常抛出
        System.out.println("[2] orDefaultOnTimeout = "
                + orDefaultOnTimeout(supplyAsync(() -> {
                    sleep(500);
                    return "slow";
                }), 100, TimeUnit.MILLISECONDS, "fallback").get());

        // 3. 异常兜底
        System.out.println("[3] fallback = " + fallback(supplyAsync(() -> {
            throw new IllegalStateException("boom");
        }), "default").get());

        // 4. 失败重试（指数退避，第 3 次成功）
        AtomicInteger attempts = new AtomicInteger();
        System.out.println("[4] retry = " + retry(() -> {
            if (attempts.incrementAndGet() < 3) {
                throw new IllegalStateException("fail-" + attempts.get());
            }
            return "ok after " + attempts.get();
        }, 3, 50, TimeUnit.MILLISECONDS).get());

        // 5. 聚合：allOf / allOfSafely / anyOfSuccess
        List<CompletableFuture<Integer>> futures = new ArrayList<>();
        futures.add(supplyAsync(() -> 1));
        futures.add(supplyAsync(() -> 2));
        System.out.println("[5] allOf = " + allOf(futures).get());

        List<CompletableFuture<Integer>> mixed = new ArrayList<>();
        mixed.add(supplyAsync(() -> 1));
        mixed.add(supplyAsync(() -> {
            throw new IllegalStateException("bad one");
        }));
        System.out.println("[5] allOfSafely = " + allOfSafely(mixed, -1).get());

        List<CompletableFuture<String>> racers = new ArrayList<>();
        racers.add(supplyAsync(() -> {
            sleep(300);
            return "slow-winner";
        }));
        racers.add(failed(new IllegalStateException("fast-failed")));
        racers.add(supplyAsync(() -> "fast-winner"));
        System.out.println("[5] anyOfSuccess = " + anyOfSuccess(racers).get());

        // 5.1 批量并行执行（按入参维度收集，入参去重、忽略null）
        List<String> names = new ArrayList<>();
        names.add("apple");
        names.add("pear");
        names.add("apple");
        System.out.println("[5] execute = " + execute(names, String::length));

        // 6. 回调式异步转 CF + 双回调监听
        Callback<String> callback = handler -> new Thread(() -> handler.onSuccess("callback-result")).start();
        CompletableFuture<String> fromCallback = toCompletableFuture(callback, null);
        onComplete(fromCallback, r -> System.out.println("[6] onSuccess = " + r),
                e -> System.out.println("[6] onFailure = " + e.getMessage()));
        sleep(200);

        System.out.println("all done");
    }

    private static void sleep(long millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
