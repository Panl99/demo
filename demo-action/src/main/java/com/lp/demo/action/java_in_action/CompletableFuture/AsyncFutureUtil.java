package com.lp.demo.action.java_in_action.CompletableFuture;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
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
 *     <li>失败重试：{@link #retry}（同步取值版）/ {@link #retryAsync}（异步调用版），支持指数退避</li>
 *     <li>聚合收敛：{@link #allOf} / {@link #allOfSafely} / {@link #allOfMap} / {@link #anyOf} / {@link #anyOfSuccess}</li>
 *     <li>二路结果组合：{@link #thenCombine} / {@link #thenCombineAsync} / {@link #thenCombineFailFast}</li>
 *     <li>批量并行执行：{@link #execute} / {@link #executeBatched} / {@link #allOfList}</li>
 *     <li>回调监听：{@link #toCompletableFuture} / {@link #onComplete}</li>
 *     <li>组合编排与并发控制：{@link #delay} / {@link #hedgedRequest} / {@link #singleFlight} / {@link #allOfWithTimeout}</li>
 * </ol>
 * <p>
 * 常用使用场景（每个场景在下方都有对应的 {@code xxxTestN} 用例）：
 * <ol>
 *     <li>接口聚合：并行调多个下游，全部成功后再统一返回 → {@code allOf}</li>
 *     <li>接口聚合（容错）：允许个别下游失败，失败项用默认值占位 → {@code allOfSafely}</li>
 *     <li>两路并行后合并：step1/step2 无依赖可并行，step3 依赖两者结果 → {@code thenCombine}</li>
 *     <li>二路组合但不想被慢下游拖住失败 → {@code thenCombineFailFast}</li>
 *     <li>批量 IO：批量查在线状态、批量拉配置，按入参维度收集结果 → {@code execute}</li>
 *     <li>下游超时降级：RPC 超时就用兜底值，别拖垮接口 RT → {@code orDefaultOnTimeout} / {@code orFallbackOnTimeout}</li>
 *     <li>非关键旁路：写操作日志、发通知，失败不影响主流程 → {@code fallback}</li>
 *     <li>瞬时故障重试：网络抖动、锁冲突、下游限流，重试几次就好 → {@code retry}</li>
 *     <li>重试"需要重新发起的异步调用"（回调式 API 等） → {@code retryAsync}</li>
 *     <li>缓存击穿防护：同 key 并发请求只回源一次 → {@code singleFlight}</li>
 *     <li>长尾优化：主请求超时后自动补发备请求，取先成功的 → {@code hedgedRequest}</li>
 *     <li>大批量分片执行，避免一次性提交撑爆池 → {@code executeBatched}</li>
 *     <li>聚合必须带整体时间预算，超时回收底层任务 → {@code allOfWithTimeout}</li>
 *     <li>多数据源竞速：多机房/主备同查，取最先返回成功的那个 → {@code anyOfSuccess}</li>
 *     <li>回调式 API 桥接：thrift / MQ / HTTP 的 callback 转成 CF 链 → {@code toCompletableFuture}</li>
 *     <li>异步链路收尾：成功计数、失败告警，且不改变原有结果 → {@code onComplete}</li>
 *     <li>线程池托管：Spring 启动时换成业务自管的池 → {@code setDefaultExecutor}</li>
 * </ol>
 * <p>
 * 已有方法之间的常见搭配、以及刻意不封装的原生算子（thenApply / thenAccept / thenRun / whenComplete 等），
 * 都不另立 API，统一以 {@code xxxComboTestN} 用例的形式给出用法，
 * 见测试用例区的「组合用法与原生算子对照」小节。
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
     * <p>
     * 场景：排查线程使用情况，或把当前默认池透传给其他组件复用。
     */
    public static Executor getDefaultExecutor() {
        return defaultExecutor;
    }

    /**
     * 替换默认线程池，传 null 表示恢复内置线程池。
     * 典型用法：Spring 容器启动后注入业务自定义的 ThreadPoolTaskExecutor。
     * <p>
     * 场景：内置默认池是通用兜底值（并行度、队列容量都是经验值），已知负载特征的服务
     * 应在启动时换成业务自管的池；也可以临时替换成测试池验证并行度影响。
     * <p>
     * 用例：{@code setDefaultExecutorTest1}
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
     * <p>
     * 场景：把一次耗时的下游调用（RPC / HTTP / DB）异步化，主线程立刻拿到 future 去做别的事。
     * <p>
     * 用例：{@code supplyAsyncTest1}
     */
    public static <T> CompletableFuture<T> supplyAsync(Supplier<T> supplier) {
        return supplyAsync(supplier, defaultExecutor);
    }

    /**
     * 提交有返回值的异步任务，使用指定线程池
     * <p>
     * 场景：需要资源隔离或自定义并行度时，显式传入业务自管的线程池。
     * <p>
     * 用例：{@code supplyAsyncTest2}
     */
    public static <T> CompletableFuture<T> supplyAsync(Supplier<T> supplier, Executor executor) {
        Objects.requireNonNull(supplier, "supplier");
        return CompletableFuture.supplyAsync(supplier, resolve(executor));
    }

    /**
     * 提交无返回值的异步任务，使用默认线程池
     * <p>
     * 场景：只关心"做完了"，不关心返回值 —— 异步落库、异步埋点、异步发通知。
     * <p>
     * 用例：{@code runAsyncTest1}
     */
    public static CompletableFuture<Void> runAsync(Runnable runnable) {
        return runAsync(runnable, defaultExecutor);
    }

    /**
     * 提交无返回值的异步任务，使用指定线程池
     * <p>
     * 场景：同 {@link #runAsync(Runnable)}，但需要隔离线程池。
     */
    public static CompletableFuture<Void> runAsync(Runnable runnable, Executor executor) {
        Objects.requireNonNull(runnable, "runnable");
        return CompletableFuture.runAsync(runnable, resolve(executor));
    }

    /**
     * 构造一个已失败的 CompletableFuture（Java 8 无 failedFuture）
     * <p>
     * 场景：编排降级分支时提前造一个失败态；单测里模拟"某个下游直接失败"。
     * <p>
     * 用例：{@code failedTest1}
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
     * <p>
     * 场景：给异步链路加硬超时 —— 下游迟迟不返回时，不能无限等下去，直接按超时失败处理。
     * <p>
     * 用例：{@code orTimeoutTest1}（超时抛异常）、{@code orTimeoutTest2}（未超时正常返回且原 future 不受影响）
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
     * <p>
     * 场景：下游超时就返回兜底值，保证接口一定有结果返回，不被一个慢下游拖垮整体 RT。
     * <p>
     * 用例：{@code orDefaultOnTimeoutTest1}（超时给默认值）、{@code orDefaultOnTimeoutTest2}（业务异常不被吞掉）
     */
    public static <T> CompletableFuture<T> orDefaultOnTimeout(CompletableFuture<T> future, long timeout,
                                                              TimeUnit unit, T defaultValue) {
        return orFallbackOnTimeout(future, timeout, unit, () -> defaultValue);
    }

    /**
     * 超时走兜底函数；非超时的业务异常仍然向上抛出，不做吞没
     * <p>
     * 场景：超时后不是简单给个常量，而是要走一段降级逻辑 —— 查本地缓存、返回上一次的稳定值、调备用下游。
     * <p>
     * 用例：{@code orFallbackOnTimeoutTest1}
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
     * <p>
     * 场景：{@code join()} / {@code get()} 抛出来的异常外面套了一层壳，直接判断类型会永远判断不上；
     * 想按业务异常类型分流（比如"查不到"和"超时"要区别对待）时必须先解包。
     * <p>
     * 用例：{@code unwrapTest1}（剥壳）、{@code unwrapTest2}（非包装异常原样返回）
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
     * <p>
     * 场景：非关键旁路兜底 —— 写操作日志、发通知、同步统计，失败了不该影响主流程。
     * <p>
     * 用例：{@code fallbackTest1}
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
     * <p>
     * 场景：下游正常返回了 null 也要按默认值处理 —— 查配置查不到给默认配置、查列表为空给空集合，
     * 避免调用方到处写 null 判断。
     * <p>
     * 用例：{@code fallbackIfNullTest1}（null 兜底）、{@code fallbackIfNullTest2}（正常值不被覆盖）
     */
    public static <T> CompletableFuture<T> fallbackIfNull(CompletableFuture<T> future, T defaultValue) {
        return fallback(future, defaultValue).thenApply(value -> value == null ? defaultValue : value);
    }

    // ==================== 4. 失败重试 ====================

    /**
     * 失败重试，默认线程池、无退避
     * <p>
     * 场景：瞬时故障重试 —— 网络抖动、连接闪断、下游限流。这类失败重试一次往往就好了，
     * 且因为失败是瞬间的，不需要退避等待。
     * <p>
     * 用例：{@code retryTest3}（不重试）、{@code retryTest4}（指定线程池重试）
     *
     * @param maxRetries 最大重试次数（不含首次执行），0 表示不重试
     */
    public static <T> CompletableFuture<T> retry(Supplier<T> supplier, int maxRetries) {
        return retry(supplier, maxRetries, 0, TimeUnit.MILLISECONDS, defaultExecutor);
    }

    /**
     * 失败重试，默认线程池、按 delay 做指数退避
     * <p>
     * 场景：下游被打挂后的重试 —— 立即重试大概率还是失败，退避后再试才有意义，
     * 指数退避还能避免大量调用方同时重试形成重试风暴。
     * <p>
     * 用例：{@code retryTest1}（指数退避后成功）、{@code retryTest2}（重试耗尽抛异常）
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

    /**
     * 异步重试（supplier 返回 CompletableFuture），无退避。
     * <p>
     * 与 {@link #retry(Supplier, int)} 的区别：那个版本的 supplier 返回"结果值"，会在池线程里同步执行，
     * 因此重试一个"需要重新发起的异步调用"时只能在里面 {@code join()} 把池线程阻塞住；
     * 本版本 supplier 直接返回 future，可以和 {@link #toCompletableFuture} 等异步入口串联，
     * 不会占用额外线程等待。
     * <p>
     * 用例：{@code retryAsyncTest2}（重试耗尽抛出异常）
     */
    public static <T> CompletableFuture<T> retryAsync(Supplier<CompletableFuture<T>> supplier, int maxRetries) {
        Objects.requireNonNull(supplier, "supplier");
        return retryAsyncInternal(supplier, Math.max(maxRetries, 0), 1, 0L, 0L);
    }

    /**
     * 异步重试，按 backoff 做指数退避。
     * <p>
     * 用例：{@code retryAsyncTest1}（退避后成功）、{@code retryAsyncTest4}（配合回调式 API 重试）
     */
    public static <T> CompletableFuture<T> retryAsync(Supplier<CompletableFuture<T>> supplier, int maxRetries,
                                                      long backoff, TimeUnit unit) {
        Objects.requireNonNull(supplier, "supplier");
        Objects.requireNonNull(unit, "unit");
        return retryAsyncInternal(supplier, Math.max(maxRetries, 0), 1, unit.toNanos(backoff), 0L);
    }

    /**
     * 异步重试，按 backoff 做指数退避，且每次尝试有独立的超时上限。
     * <p>
     * 场景：下游偶发卡住 —— 一次尝试超过 attemptTimeout 就按失败处理并重试，
     * 避免一次卡死的调用把整个重试预算耗尽。
     * <p>
     * 用例：{@code retryAsyncTest3}
     */
    public static <T> CompletableFuture<T> retryAsync(Supplier<CompletableFuture<T>> supplier, int maxRetries,
                                                      long backoff, TimeUnit backoffUnit,
                                                      long attemptTimeout, TimeUnit timeoutUnit) {
        Objects.requireNonNull(supplier, "supplier");
        Objects.requireNonNull(backoffUnit, "backoffUnit");
        Objects.requireNonNull(timeoutUnit, "timeoutUnit");
        return retryAsyncInternal(supplier, Math.max(maxRetries, 0), 1,
                backoffUnit.toNanos(backoff), timeoutUnit.toNanos(attemptTimeout));
    }

    private static <T> CompletableFuture<T> retryAsyncInternal(Supplier<CompletableFuture<T>> supplier,
                                                               int maxRetries, int attempt,
                                                               long baseBackoffNanos, long attemptTimeoutNanos) {
        CompletableFuture<T> current = invokeSafely(supplier);
        if (attemptTimeoutNanos > 0) {
            current = orTimeout(current, attemptTimeoutNanos, TimeUnit.NANOSECONDS);
        }
        return current.handle((value, throwable) -> {
            if (throwable == null) {
                return CompletableFuture.completedFuture(value);
            }
            Throwable real = unwrap(throwable);
            if (attempt > maxRetries) {
                log.error("async task retry exhausted, attempts:{}, error:{}", attempt, real.getMessage());
                return AsyncFutureUtil.<T>failed(real);
            }
            long backoffNanos = backoffNanos(baseBackoffNanos, attempt);
            log.warn("async task failed, retry {}/{}, backoff {}ms, error:{}",
                    attempt, maxRetries, backoffNanos / 1_000_000, real.getMessage());
            return delayAfter(backoffNanos).thenCompose(v -> retryAsyncInternal(supplier, maxRetries, attempt + 1,
                    baseBackoffNanos, attemptTimeoutNanos));
        }).thenCompose(future -> future);
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
     * <p>
     * 场景：接口聚合 —— 详情页要同时拿用户、订单、商品三份数据，三份都成功才拼装返回；
     * 任意一份拿不到，整体就没意义，应该直接失败。
     * <p>
     * 用例：{@code allOfTest1}（全部成功且顺序对齐）、{@code allOfTest2}（任一失败整体失败）、
     * {@code allOfTest3}（空集合返回空 list）
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
     * <p>
     * 场景：聚合但允许部分失败 —— 批量查 100 台设备状态，其中 3 台查询失败，
     * 不该让整个列表查询报错，失败项用默认值占位即可（顺序仍与入参一一对应）。
     * <p>
     * 用例：{@code allOfSafelyTest1}
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
     * <p>
     * 场景：分片查询后合并 —— 按设备维度分成多批并行查配置，每批各返回一个 Map，最后合并成完整字典。
     * <p>
     * 用例：{@code allOfMapTest1}
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
     * <p>
     * 场景：只关心"最先到达的回包"，成功失败都要立刻知道 —— 例如探测多个节点哪个先响应。
     * 若只想要"最先成功"的结果，请用 {@link #anyOfSuccess}。
     * <p>
     * 用例：{@code anyOfTest1}
     */
    @SuppressWarnings("unchecked")
    public static <T> CompletableFuture<T> anyOf(Collection<CompletableFuture<T>> futures) {
        Objects.requireNonNull(futures, "futures");
        return (CompletableFuture<T>) CompletableFuture.anyOf(futures.toArray(new CompletableFuture<?>[0]));
    }

    /**
     * 竞速：取第一个"成功"的结果，全部失败时以最后一个异常结束。
     * 入参为空时以 {@link IllegalArgumentException} 结束。
     * <p>
     * 场景：多数据源竞速 —— 多机房 / 主备同查，谁先成功用谁；失败的副本自动忽略，不拖慢整体。
     * <p>
     * 用例：{@code anyOfSuccessTest1}（取首个成功并跳过失败）、{@code anyOfSuccessTest2}（全部失败）、
     * {@code anyOfSuccessTest3}（空集合）
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
     * <p>
     * 场景：手里已经有一批 {@link Supplier}（比如从多个数据源组装出的查询任务），
     * 想一次性并行提交并按下标拿到结果。
     * <p>
     * 用例：{@code allOfListTest1}
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
     * <p>
     * 场景：批量 IO —— 批量查设备在线状态、批量拉配置、批量调同一个接口且每台设备的处理彼此独立。
     * <p>
     * 用例：{@code executeTest1}（按入参收集 + 去重）、{@code executeTest3}（空集合）、
     * {@code executeTest4}（任务返回 null 不收集）、{@code executeTest5}（任务异常向上抛）
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
     * <p>
     * 场景：同 {@link #execute(Collection, Function)}，但批量操作的并行度需要与业务其他异步任务隔离
     * （例如批量网关操作不应抢占设备消息处理的线程）。
     * <p>
     * 用例：{@code executeTest2}
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

    /**
     * 分批执行批量任务，每批最多 batchSize 个，使用默认线程池。
     * <p>
     * 场景：批量处理上万条数据 —— {@link #execute(Collection, Function)} 是一次性全量提交，
     * 超出"线程数 + 队列容量"的部分会走 CallerRunsPolicy 由调用线程自己执行，导致提交方被阻塞；
     * 分批提交可以把在途任务量和调用方阻塞时间都控制在预期内。
     * <p>
     * 阻塞式（内部 {@code join}），收集语义与 {@link #execute(Collection, Function)} 完全一致。
     * <p>
     * 用例：{@code executeBatchedTest1}、{@code executeBatchedTest2}
     */
    public static <T, R> Map<T, R> executeBatched(Collection<T> items, Function<T, R> task, int batchSize) {
        return executeBatched(items, task, batchSize, defaultExecutor);
    }

    /**
     * 分批执行批量任务，使用指定线程池
     *
     * @param batchSize 每批提交的任务数，小于等于 0 时按 1 处理
     */
    public static <T, R> Map<T, R> executeBatched(Collection<T> items, Function<T, R> task, int batchSize,
                                                  Executor executor) {
        if (items == null || items.isEmpty() || task == null) {
            return Collections.emptyMap();
        }
        List<T> unique = items.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (unique.isEmpty()) {
            return Collections.emptyMap();
        }
        int size = Math.max(batchSize, 1);
        Map<T, R> result = new ConcurrentHashMap<>();
        for (int from = 0; from < unique.size(); from += size) {
            int to = Math.min(from + size, unique.size());
            result.putAll(execute(unique.subList(from, to), task, executor));
        }
        return result;
    }

    // ---------- 二路结果组合（thenCombine） ----------

    /**
     * 两个 future 都完成后，用 combiner 合并各自的结果。
     * <p>
     * 场景：两路并行后拼装 —— step1 与 step2 无依赖可并行，step3 需要两者结果才能执行
     * （典型的"并行两路查数据，再组装成详情"）。
     * <p>
     * 语义与原生 {@code first.thenCombine(second, combiner)} 完全一致，注意其中的坑：
     * <b>失败不短路</b>。任意一侧失败后，结果 future 仍要等另一侧也结束才会失败；
     * 一侧已失败、另一侧还在慢慢跑时，异常会被一直拖住。
     * 需要"一侧失败立即失败"请用 {@link #thenCombineFailFast}。
     * <p>
     * 用例：{@code thenCombineTest1}（两路并行后合并）、{@code thenCombineTest2}（一侧失败整体失败）、
     * {@code thenCombineTest3}（失败不短路）、{@code thenCombineTest4}（合并函数抛异常）、
     * {@code thenCombineTest5}（原 future 不被污染）
     */
    public static <A, B, R> CompletableFuture<R> thenCombine(CompletableFuture<A> first,
                                                             CompletableFuture<B> second,
                                                             BiFunction<A, B, R> combiner) {
        Objects.requireNonNull(first, "first");
        Objects.requireNonNull(second, "second");
        Objects.requireNonNull(combiner, "combiner");
        return first.thenCombine(second, combiner);
    }

    /**
     * 两个 future 都完成后合并结果，合并逻辑放到指定线程池执行（executor 传 null 走默认池）。
     * <p>
     * 场景：合并逻辑本身偏重（大量计算、二次远程调用），不想占用"恰好完成第二路"的那个线程；
     * 或需要与业务其他异步任务隔离时显式传入自管的池。
     * <p>
     * 与原生 {@code thenCombineAsync} 一样，失败同样不短路。
     * <p>
     * 用例：{@code thenCombineAsyncTest1}（合并逻辑在指定池执行）、
     * {@code thenCombineAsyncTest2}（executor 为 null 时走默认池）
     */
    public static <A, B, R> CompletableFuture<R> thenCombineAsync(CompletableFuture<A> first,
                                                                  CompletableFuture<B> second,
                                                                  BiFunction<A, B, R> combiner,
                                                                  Executor executor) {
        Objects.requireNonNull(first, "first");
        Objects.requireNonNull(second, "second");
        Objects.requireNonNull(combiner, "combiner");
        return first.thenCombineAsync(second, combiner, resolve(executor));
    }

    /**
     * 两个 future 都完成后合并结果，但<b>任意一侧失败立即失败</b>，不再等另一侧。
     * <p>
     * 场景：聚合时不想被一个慢下游拖住失败 —— 例如主备两路查询，一路已经明确报错，
     * 没必要等另一路超时才返回错误，接口可以更早失败并触发降级。
     * <p>
     * 这是原生 {@code thenCombine} 给不了的能力（原生必须等两侧都进入终态）。
     * <p>
     * 用例：{@code thenCombineFailFastTest1}（两侧都成功正常合并）、
     * {@code thenCombineFailFastTest2}（一侧失败立即结束）、
     * {@code thenCombineFailFastTest3}（两侧都成功但合并函数抛异常）、
     * {@code thenCombineFailFastTest4}（两侧都失败）
     */
    public static <A, B, R> CompletableFuture<R> thenCombineFailFast(CompletableFuture<A> first,
                                                                     CompletableFuture<B> second,
                                                                     BiFunction<A, B, R> combiner) {
        Objects.requireNonNull(first, "first");
        Objects.requireNonNull(second, "second");
        Objects.requireNonNull(combiner, "combiner");
        CompletableFuture<R> result = new CompletableFuture<>();
        // 任意一侧失败即让结果失败，不再等另一侧
        first.whenComplete((value, throwable) -> {
            if (throwable != null) {
                result.completeExceptionally(unwrap(throwable));
            }
        });
        second.whenComplete((value, throwable) -> {
            if (throwable != null) {
                result.completeExceptionally(unwrap(throwable));
            }
        });
        // 两侧都完成后的收口：成功则合并结果；失败则补上"两侧都成功、但 combiner 自己抛错"的情况，
        // 否则那种场景下 result 会因为两侧的 whenComplete 都没触发而永不完成
        first.thenCombine(second, combiner).whenComplete((value, throwable) -> {
            if (throwable == null) {
                result.complete(value);
            } else {
                result.completeExceptionally(unwrap(throwable));
            }
        });
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
     * <p>
     * 场景：thrift / MQ / HTTP 客户端这类"注册回调"风格的 API，转成 CF 后就能直接接上
     * {@link #orTimeout} / {@link #retry} / {@link #allOf} 等能力，不用再手写回调嵌套。
     * <p>
     * 用例：{@code toCompletableFutureTest1}（回调成功）、{@code toCompletableFutureTest2}（回调失败）、
     * {@code toCompletableFutureTest3}（发起调用即抛异常）
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
     * <p>
     * 场景：异步链路收尾 —— 成功时打点计数、失败时告警上报，这些动作不该改变链路的原有结果
     * （成功还是成功，失败还是失败），所以用 {@code whenComplete} 而不是 {@code handle}。
     * <p>
     * 用例：{@code onCompleteTest1}（成功回调）、{@code onCompleteTest2}（失败回调且不改变结果）
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

    // ==================== 7. 组合编排与并发控制 ====================

    /**
     * 同一个 key 上正在进行的加载任务（singleFlight 使用）
     */
    private static final ConcurrentHashMap<Object, CompletableFuture<?>> IN_FLIGHT = new ConcurrentHashMap<>();

    /**
     * 延迟指定时间后完成的 future，可用作定时续接的起点。
     * <p>
     * 场景：对冲请求（主请求超时后再发备请求）、错峰重试、定时续接。
     * 返回的 future 可以被 cancel，取消后它的下游续接不会执行。
     * <p>
     * 用例：{@code delayTest1}
     */
    public static CompletableFuture<Void> delay(long delay, TimeUnit unit) {
        Objects.requireNonNull(unit, "unit");
        return delayAfter(unit.toNanos(delay));
    }

    /**
     * 对冲请求：先发主请求，超过 hedgeDelay 主请求仍未成功就再发一个备请求，取先成功的那一个；
     * 两个都失败时整体失败。
     * <p>
     * 场景：长尾优化 —— 单条链路偶发慢查询时，用一次额外的冗余请求把 P99 拉下来。
     * <p>
     * 行为细节：
     * <ul>
     *     <li>主请求先成功 -> 备请求不再发出（对冲窗口被取消），不产生多余的冗余流量；</li>
     *     <li>主请求先失败 -> 备请求立即发出，不用等满 hedgeDelay；</li>
     *     <li>备请求发出后主请求才成功 -> 以先成功者为准，另一条不会被打断。</li>
     * </ul>
     * <p>
     * 用例：{@code hedgedRequestTest1}（备请求胜出）、{@code hedgedRequestTest2}（主请求胜出且备请求不发）
     */
    public static <T> CompletableFuture<T> hedgedRequest(Supplier<CompletableFuture<T>> primary,
                                                         Supplier<CompletableFuture<T>> backup,
                                                         long hedgeDelay, TimeUnit unit) {
        Objects.requireNonNull(primary, "primary");
        Objects.requireNonNull(backup, "backup");
        Objects.requireNonNull(unit, "unit");
        CompletableFuture<T> primaryFuture = invokeSafely(primary);
        CompletableFuture<Void> hedgeGate = delayAfter(unit.toNanos(hedgeDelay));
        primaryFuture.whenComplete((value, throwable) -> {
            if (throwable == null) {
                hedgeGate.cancel(false);
            } else {
                hedgeGate.complete(null);
            }
        });
        CompletableFuture<T> backupFuture = hedgeGate.thenCompose(v -> invokeSafely(backup));
        return anyOfSuccess(Arrays.asList(primaryFuture, backupFuture));
    }

    /**
     * 请求合并（single-flight）：同一个 key 上同时只允许一个加载任务在途，
     * 并发的调用方共享同一个 future，只有第一个调用方真正触发加载。
     * <p>
     * 场景：热点 key 的缓存击穿 —— 缓存失效的瞬间涌入 100 个请求，若各自回源就是 100 次下游调用，
     * 用它可以把它们压成 1 次。
     * <p>
     * 行为细节：
     * <ul>
     *     <li>任务完成后自动从在途表中移除，下次调用会重新加载；</li>
     *     <li>加载函数抛异常时同样会清理，不会把 key 卡死；</li>
     *     <li>并发竞争时以先注册的为准，后注册者本次的加载结果作废（可能产生一次多余的下游调用）；</li>
     *     <li>建议 loader 返回的 future 自带超时（{@link #orTimeout}），否则任务永久不完成会导致该 key
     *     一直无法重新加载。</li>
     * </ul>
     * <p>
     * 用例：{@code singleFlightTest1}（并发共享同一个 future 且只加载一次）、
     * {@code singleFlightTest2}（完成后 key 被清理可重新加载）、
     * {@code singleFlightTest3}（加载抛异常后 key 被清理）
     */
    @SuppressWarnings("unchecked")
    public static <T> CompletableFuture<T> singleFlight(Object key, Supplier<CompletableFuture<T>> loader) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(loader, "loader");
        CompletableFuture<T> inFlight = (CompletableFuture<T>) IN_FLIGHT.get(key);
        if (inFlight != null) {
            return inFlight;
        }
        CompletableFuture<T> created = invokeSafely(loader);
        CompletableFuture<T> previous = (CompletableFuture<T>) IN_FLIGHT.putIfAbsent(key, created);
        if (previous != null) {
            // 并发竞争：以先注册的为准，本次加载作废
            return previous;
        }
        // 完成后清理在途记录。清理必须注册在 putIfAbsent 之后（锁外），否则当 future 已经完成时，
        // 会出现在 computeIfAbsent 的锁内同步触发清理、导致"递归更新"异常
        created.whenComplete((value, throwable) -> IN_FLIGHT.remove(key, created));
        return created;
    }

    /**
     * 聚合多个 future 并带整体超时。
     * <p>
     * 场景：接口聚合必须给整体设预算 —— 3 个下游并行查最多等 500ms，超时就走降级
     * （降级可再接 {@link #orDefaultOnTimeout}）。
     * <p>
     * {@code cancelOnTimeout = true} 时会在超时后取消尚未完成的底层任务，避免慢下游继续占用线程资源。
     * <b>仅当这些 future 是本次独占创建时才能开启</b>：如果它们被其他调用方共享
     * （例如 {@link #singleFlight} 复用的在途任务），取消会误伤别的调用方。
     * <p>
     * 用例：{@code allOfWithTimeoutTest1}（未超时正常返回）、{@code allOfWithTimeoutTest2}（超时抛异常）、
     * {@code allOfWithTimeoutTest3}（超时取消底层任务）、{@code allOfWithTimeoutTest4}（非超时失败不误取消）
     */
    public static <T> CompletableFuture<List<T>> allOfWithTimeout(Collection<CompletableFuture<T>> futures,
                                                                  long timeout, TimeUnit unit,
                                                                  boolean cancelOnTimeout) {
        Objects.requireNonNull(futures, "futures");
        Objects.requireNonNull(unit, "unit");
        List<CompletableFuture<T>> list = new ArrayList<>(futures);
        CompletableFuture<List<T>> guarded = orTimeout(allOf(list), timeout, unit);
        if (!cancelOnTimeout) {
            return guarded;
        }
        return guarded.whenComplete((value, throwable) -> {
            if (throwable != null && isTimeout(unwrap(throwable))) {
                for (CompletableFuture<T> future : list) {
                    if (!future.isDone()) {
                        future.cancel(true);
                    }
                }
            }
        });
    }

    /**
     * 安全地发起一次异步调用：把"加载函数直接抛异常"和"返回 null"统一收敛成失败态 future
     */
    private static <T> CompletableFuture<T> invokeSafely(Supplier<CompletableFuture<T>> supplier) {
        try {
            CompletableFuture<T> future = supplier.get();
            return future == null
                    ? AsyncFutureUtil.<T>failed(new NullPointerException("supplier returned null"))
                    : future;
        } catch (Throwable throwable) {
            return AsyncFutureUtil.<T>failed(unwrap(throwable));
        }
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

    // ==================== 测试用例 ====================
    // 命名规则：被测试的方法名 + TestN。每个用例自带断言，不满足即抛 AssertionError，
    // 全部用例由 main 依次执行，也可以单独调用某一个做定点验证。

    // ---------- 1. 基础异步 ----------

    /**
     * 场景：把一次耗时的下游调用异步化，主线程立刻拿到 future 去做别的事。
     */
    public static void supplyAsyncTest1() throws Exception {
        System.out.println("[supplyAsyncTest1] 场景：把一次下游调用异步化，主线程不阻塞");
        CompletableFuture<String> future = supplyAsync(() -> "hello");
        checkEquals("HELLO", future.thenApply(String::toUpperCase).get(), "异步结果经 thenApply 转换");
    }

    /**
     * 场景：需要资源隔离或自定义并行度时，显式传入业务自管的线程池。
     */
    public static void supplyAsyncTest2() throws Exception {
        System.out.println("[supplyAsyncTest2] 场景：显式指定线程池，任务不落到默认池上");
        ExecutorService pool = Executors.newSingleThreadExecutor(r -> new Thread(r, "test-pool-"));
        try {
            AtomicReference<String> threadName = new AtomicReference<>();
            String value = supplyAsync(() -> {
                threadName.set(Thread.currentThread().getName());
                return "isolated";
            }, pool).get();
            checkEquals("isolated", value, "在指定池中执行并返回结果");
            checkTrue(threadName.get().startsWith("test-pool-"),
                    "执行线程来自指定池：" + threadName.get());
        } finally {
            pool.shutdownNow();
        }
    }

    /**
     * 场景：只关心"做完了"，不关心返回值 —— 异步落库、异步埋点、异步发通知。
     */
    public static void runAsyncTest1() throws Exception {
        System.out.println("[runAsyncTest1] 场景：无返回值的异步任务");
        AtomicReference<String> mark = new AtomicReference<>("init");
        runAsync(() -> mark.set("done")).get();
        checkEquals("done", mark.get(), "runAsync 的任务确实执行了");

        AtomicReference<String> after = new AtomicReference<>("init");
        supplyAsync(() -> 100).thenRun(() -> after.set("thenRun")).get();
        checkEquals("thenRun", after.get(), "thenRun 在上个任务完成后执行（不接收上个任务的结果）");
    }

    /**
     * 场景：编排降级分支时提前造一个失败态；单测里模拟"某个下游直接失败"。
     */
    public static void failedTest1() {
        System.out.println("[failedTest1] 场景：直接构造一个已失败的 future");
        CompletableFuture<String> future = failed(new IllegalStateException("下游直接失败"));
        checkTrue(future.isCompletedExceptionally(), "future 处于失败态");
        checkThrows(IllegalStateException.class, future::join, "join 时抛出解包后的业务异常");
    }

    // ---------- 2. 超时控制 ----------

    /**
     * 场景：给异步链路加硬超时，下游迟迟不返回时不能无限等下去。
     */
    public static void orTimeoutTest1() {
        System.out.println("[orTimeoutTest1] 场景：下游迟迟不返回，超时即失败");
        CompletableFuture<String> slow = supplyAsync(() -> {
            sleep(500);
            return "slow";
        });
        CompletableFuture<String> guarded = orTimeout(slow, 100, TimeUnit.MILLISECONDS);
        checkThrows(TimeoutException.class, guarded::join, "超时后以 TimeoutException 结束");
    }

    /**
     * 场景：未超时的链路行为应与非包装版完全一致；同时验证原 future 不被污染。
     */
    public static void orTimeoutTest2() throws Exception {
        System.out.println("[orTimeoutTest2] 场景：未超时正常返回，且原 future 不被污染");
        CompletableFuture<String> origin = supplyAsync(() -> "fast");
        CompletableFuture<String> guarded = orTimeout(origin, 2, TimeUnit.SECONDS);
        checkEquals("fast", guarded.get(), "未超时时正常返回结果");
        checkEquals("fast", origin.get(), "原 future 状态未被污染，仍可正常取值");
    }

    /**
     * 场景：下游超时就返回兜底值，保证接口一定有结果返回。
     */
    public static void orDefaultOnTimeoutTest1() throws Exception {
        System.out.println("[orDefaultOnTimeoutTest1] 场景：下游超时返回默认值，不被慢下游拖垮 RT");
        CompletableFuture<String> slow = supplyAsync(() -> {
            sleep(500);
            return "slow";
        });
        checkEquals("兜底值", orDefaultOnTimeout(slow, 100, TimeUnit.MILLISECONDS, "兜底值").get(),
                "超时后拿到默认值");
    }

    /**
     * 场景：只有超时才降级，真正的业务异常不能被静默吞掉。
     */
    public static void orDefaultOnTimeoutTest2() {
        System.out.println("[orDefaultOnTimeoutTest2] 场景：非超时的业务异常仍然抛出，不做吞没");
        CompletableFuture<String> boom = supplyAsync(() -> {
            throw new IllegalStateException("业务异常");
        });
        CompletableFuture<String> result = orDefaultOnTimeout(boom, 2, TimeUnit.SECONDS, "兜底值");
        checkThrows(IllegalStateException.class, result::join, "业务异常未被兜底值吞掉");
    }

    /**
     * 场景：超时后不是给常量，而是走一段降级逻辑 —— 查本地缓存、返回上次的稳定值。
     */
    public static void orFallbackOnTimeoutTest1() throws Exception {
        System.out.println("[orFallbackOnTimeoutTest1] 场景：超时后走兜底函数（这里模拟查本地缓存）");
        Map<String, String> localCache = new HashMap<>();
        localCache.put("config", "cache-value");
        CompletableFuture<String> slow = supplyAsync(() -> {
            sleep(500);
            return "remote-value";
        });
        String value = orFallbackOnTimeout(slow, 100, TimeUnit.MILLISECONDS,
                () -> localCache.getOrDefault("config", "none")).get();
        checkEquals("cache-value", value, "超时后执行兜底函数，拿到降级值");
    }

    // ---------- 3. 异常兜底与解包 ----------

    /**
     * 场景：join/get 抛出的异常外面套了一层壳，按业务异常类型分流前必须先解包。
     */
    public static void unwrapTest1() {
        System.out.println("[unwrapTest1] 场景：剥掉 CompletionException 外壳拿到真实异常");
        IllegalStateException real = new IllegalStateException("真实业务异常");
        checkTrue(unwrap(new CompletionException(real)) == real, "解包后拿到的是原始异常对象");
    }

    /**
     * 场景：异常本来就不是包装类型时，unwrap 不应改变它。
     */
    public static void unwrapTest2() {
        System.out.println("[unwrapTest2] 场景：非包装异常原样返回");
        RuntimeException plain = new RuntimeException("无包装");
        checkTrue(unwrap(plain) == plain, "非 CompletionException / ExecutionException 时原样返回");
    }

    /**
     * 场景：非关键旁路兜底 —— 写操作日志、发通知、同步统计失败不该影响主流程。
     */
    public static void fallbackTest1() throws Exception {
        System.out.println("[fallbackTest1] 场景：非关键旁路失败，静默降级不影响主流程");
        CompletableFuture<String> boom = supplyAsync(() -> {
            throw new IllegalStateException("旁路失败");
        });
        checkEquals("静默跳过", fallback(boom, "静默跳过").get(), "失败后返回默认值");
    }

    /**
     * 场景：下游正常返回 null 也要按默认值处理，避免调用方到处写 null 判断。
     */
    public static void fallbackIfNullTest1() throws Exception {
        System.out.println("[fallbackIfNullTest1] 场景：下游返回 null，替换为默认值");
        CompletableFuture<String> nullFuture = supplyAsync(() -> null);
        checkEquals("默认配置", fallbackIfNull(nullFuture, "默认配置").get(), "null 被替换为默认值");
    }

    /**
     * 场景：有真实值时必须保留原值，不能被默认值覆盖。
     */
    public static void fallbackIfNullTest2() throws Exception {
        System.out.println("[fallbackIfNullTest2] 场景：正常值不被默认值覆盖");
        checkEquals("真实值", fallbackIfNull(supplyAsync(() -> "真实值"), "默认值").get(), "非 null 时保留原值");
    }

    // ---------- 4. 失败重试 ----------

    /**
     * 场景：下游瞬时故障，指数退避后重试成功，避免立即重试继续失败。
     */
    public static void retryTest1() throws Exception {
        System.out.println("[retryTest1] 场景：前两次失败，指数退避后第 3 次成功");
        AtomicInteger attempts = new AtomicInteger();
        long start = System.currentTimeMillis();
        String value = retry(() -> {
            if (attempts.incrementAndGet() < 3) {
                throw new IllegalStateException("第 " + attempts.get() + " 次失败");
            }
            return "第 " + attempts.get() + " 次成功";
        }, 3, 50, TimeUnit.MILLISECONDS).get();
        long cost = System.currentTimeMillis() - start;

        checkEquals("第 3 次成功", value, "最终重试成功并返回结果");
        checkEquals(3, attempts.get(), "共执行 3 次（1 次首发 + 2 次重试）");
        checkTrue(cost >= 150, "指数退避生效：50ms + 100ms = 150ms，实测 " + cost + "ms");
    }

    /**
     * 场景：重试次数耗尽后异常必须向上抛，不能静默返回成功。
     */
    public static void retryTest2() {
        System.out.println("[retryTest2] 场景：重试耗尽后抛出异常");
        AtomicInteger attempts = new AtomicInteger();
        CompletableFuture<String> future = retry(() -> {
            attempts.incrementAndGet();
            throw new IllegalStateException("始终失败");
        }, 2, 10, TimeUnit.MILLISECONDS);

        checkThrows(IllegalStateException.class, future::join, "重试耗尽后抛出业务异常");
        checkEquals(3, attempts.get(), "共执行 3 次（1 次首发 + 2 次重试）");
    }

    /**
     * 场景：明确不该重试的场合（例如非幂等的写操作）。
     */
    public static void retryTest3() {
        System.out.println("[retryTest3] 场景：maxRetries = 0 表示不重试");
        AtomicInteger attempts = new AtomicInteger();
        CompletableFuture<String> future = retry(() -> {
            attempts.incrementAndGet();
            throw new IllegalStateException("失败");
        }, 0);

        checkThrows(IllegalStateException.class, future::join, "不重试时直接抛出异常");
        checkEquals(1, attempts.get(), "只执行了 1 次");
    }

    /**
     * 场景：重试任务放到业务自管的线程池里执行。
     */
    public static void retryTest4() throws Exception {
        System.out.println("[retryTest4] 场景：指定线程池进行重试");
        ExecutorService pool = Executors.newFixedThreadPool(4, r -> new Thread(r, "test-retry-pool-"));
        try {
            AtomicReference<String> threadName = new AtomicReference<>();
            AtomicInteger attempts = new AtomicInteger();
            String value = retry(() -> {
                threadName.set(Thread.currentThread().getName());
                if (attempts.incrementAndGet() < 2) {
                    throw new IllegalStateException("第一次失败");
                }
                return "ok";
            }, 2, 10, TimeUnit.MILLISECONDS, pool).get();

            checkEquals("ok", value, "指定池中重试后成功");
            checkTrue(threadName.get().startsWith("test-retry-pool-"),
                    "执行线程来自指定池：" + threadName.get());
        } finally {
            pool.shutdownNow();
        }
    }

    /**
     * 场景：重试"需要重新发起的异步调用" —— supplier 返回 future，不需要 join 阻塞池线程。
     */
    public static void retryAsyncTest1() throws Exception {
        System.out.println("[retryAsyncTest1] 场景：异步调用重试，指数退避后成功");
        AtomicInteger attempts = new AtomicInteger();
        long start = System.currentTimeMillis();
        String value = retryAsync(() -> {
            int current = attempts.incrementAndGet();
            return supplyAsync(() -> {
                if (current < 3) {
                    throw new IllegalStateException("第 " + current + " 次失败");
                }
                return "第 " + current + " 次成功";
            });
        }, 2, 50, TimeUnit.MILLISECONDS).get();
        long cost = System.currentTimeMillis() - start;

        checkEquals("第 3 次成功", value, "异步调用最终重试成功");
        checkEquals(3, attempts.get(), "共发起 3 次异步调用");
        checkTrue(cost >= 150, "指数退避生效：50ms + 100ms = 150ms，实测 " + cost + "ms");
    }

    /**
     * 场景：异步调用重试次数耗尽后，异常向上抛出。
     */
    public static void retryAsyncTest2() {
        System.out.println("[retryAsyncTest2] 场景：异步调用重试耗尽后抛异常");
        AtomicInteger attempts = new AtomicInteger();
        CompletableFuture<String> future = retryAsync(() -> {
            attempts.incrementAndGet();
            return supplyAsync(() -> {
                throw new IllegalStateException("始终失败");
            });
        }, 2, 10, TimeUnit.MILLISECONDS);

        checkThrows(IllegalStateException.class, future::join, "重试耗尽后抛出业务异常");
        checkEquals(3, attempts.get(), "共发起 3 次异步调用");
    }

    /**
     * 场景：下游偶发卡住 —— 单次尝试超时就按失败处理并重试，避免一次卡死耗尽全部重试预算。
     */
    public static void retryAsyncTest3() throws Exception {
        System.out.println("[retryAsyncTest3] 场景：单次尝试超时也触发重试");
        AtomicInteger attempts = new AtomicInteger();
        String value = retryAsync(() -> {
            int current = attempts.incrementAndGet();
            return supplyAsync(() -> {
                if (current < 3) {
                    sleep(300);
                    return "slow-" + current;
                }
                return "fast-" + current;
            });
        }, 3, 20, TimeUnit.MILLISECONDS, 100, TimeUnit.MILLISECONDS).get();

        checkEquals("fast-3", value, "前两次因单次尝试超时失败，第 3 次成功");
        checkEquals(3, attempts.get(), "共发起 3 次异步调用");
    }

    /**
     * 场景：回调式 API 按异步方式重试 —— 与 {@link #toCompletableFuture} 直接串联。
     */
    public static void retryAsyncTest4() throws Exception {
        System.out.println("[retryAsyncTest4] 场景：回调式 API 按异步方式重试");
        AtomicInteger attempts = new AtomicInteger();
        CompletableFuture<String> future = retryAsync(() -> {
            int current = attempts.incrementAndGet();
            Callback<String> callback = handler -> new Thread(() -> {
                if (current < 2) {
                    handler.onFailure(new IllegalStateException("回调失败-" + current));
                } else {
                    handler.onSuccess("callback-ok-" + current);
                }
            }).start();
            return toCompletableFuture(callback, null);
        }, 2, 10, TimeUnit.MILLISECONDS);

        checkEquals("callback-ok-2", future.get(), "回调式 API 重试成功后拿到结果");
        checkEquals(2, attempts.get(), "共发起 2 次回调式调用");
    }

    // ---------- 5. 聚合收敛 ----------

    /**
     * 场景：接口聚合 —— 详情页同时拿用户、订单、商品，都成功才拼装返回。
     */
    public static void allOfTest1() throws Exception {
        System.out.println("[allOfTest1] 场景：多个下游全部成功才返回，结果顺序与入参一致");
        List<CompletableFuture<Integer>> futures = new ArrayList<>();
        futures.add(supplyAsync(() -> 1));
        futures.add(supplyAsync(() -> 2));
        futures.add(supplyAsync(() -> 3));
        checkEquals(Arrays.asList(1, 2, 3), allOf(futures).get(), "结果顺序与入参顺序完全一致");
    }

    /**
     * 场景：聚合中任意一份数据拿不到，整体就没意义，应该直接失败。
     */
    public static void allOfTest2() {
        System.out.println("[allOfTest2] 场景：任一失败则整体失败");
        List<CompletableFuture<Integer>> futures = new ArrayList<>();
        futures.add(supplyAsync(() -> 1));
        futures.add(supplyAsync(() -> {
            throw new IllegalStateException("第二个下游失败");
        }));
        checkThrows(IllegalStateException.class, allOf(futures)::join, "整体失败并抛出根因");
    }

    /**
     * 场景：空集合边界，不应抛异常。
     */
    public static void allOfTest3() throws Exception {
        System.out.println("[allOfTest3] 场景：入参为空集合的边界");
        List<CompletableFuture<Integer>> empty = Collections.emptyList();
        checkEquals(Collections.emptyList(), allOf(empty).get(), "空集合返回空 list，不抛异常");
    }

    /**
     * 场景：聚合但允许部分失败 —— 批量查 100 台设备状态，其中几台失败不该让整个接口报错。
     */
    public static void allOfSafelyTest1() throws Exception {
        System.out.println("[allOfSafelyTest1] 场景：部分失败不整体失败，失败项用默认值占位");
        List<CompletableFuture<Integer>> futures = new ArrayList<>();
        futures.add(supplyAsync(() -> 1));
        futures.add(supplyAsync(() -> {
            throw new IllegalStateException("第 2 个任务失败");
        }));
        futures.add(supplyAsync(() -> 3));
        checkEquals(Arrays.asList(1, -1, 3), allOfSafely(futures, -1).get(),
                "失败项降级为默认值，顺序仍与入参一一对应");
    }

    /**
     * 场景：分片查询后合并 —— 按维度分批并行查，每批各返回一个 Map，最后合并成完整字典。
     */
    public static void allOfMapTest1() throws Exception {
        System.out.println("[allOfMapTest1] 场景：多批分片查询结果合并为一个 Map");
        Map<String, Integer> first = new HashMap<>();
        first.put("a", 1);
        first.put("dup", 1);
        Map<String, Integer> second = new HashMap<>();
        second.put("b", 2);
        second.put("dup", 2);

        List<CompletableFuture<Map<String, Integer>>> futures = new ArrayList<>();
        futures.add(supplyAsync(() -> first));
        futures.add(supplyAsync(() -> second));

        Map<String, Integer> merged = allOfMap(futures).get();
        checkEquals(3, merged.size(), "两个 Map 合并后共 3 个 key");
        checkEquals(2, merged.get("dup"), "key 冲突时后写入的覆盖先写入的");
    }

    /**
     * 场景：只关心"最先到达的回包"，成功失败都要立刻知道（例如探测多个节点哪个先响应）。
     */
    public static void anyOfTest1() throws Exception {
        System.out.println("[anyOfTest1] 场景：只要最快完成的那一个，不区分成功失败");
        List<CompletableFuture<String>> futures = new ArrayList<>();
        futures.add(supplyAsync(() -> {
            sleep(300);
            return "slow";
        }));
        futures.add(supplyAsync(() -> "fast"));
        checkEquals("fast", anyOf(futures).get(), "返回最先完成的结果");
    }

    /**
     * 场景：多机房 / 主备竞速，谁先成功用谁，失败的副本自动忽略。
     */
    public static void anyOfSuccessTest1() throws Exception {
        System.out.println("[anyOfSuccessTest1] 场景：取第一个成功的结果，跳过失败的副本");
        List<CompletableFuture<String>> futures = new ArrayList<>();
        futures.add(supplyAsync(() -> {
            sleep(300);
            return "慢副本";
        }));
        futures.add(failed(new IllegalStateException("快副本失败")));
        futures.add(supplyAsync(() -> "快且成功的副本"));
        checkEquals("快且成功的副本", anyOfSuccess(futures).get(), "跳过失败副本，取第一个成功的");
    }

    /**
     * 场景：所有副本都挂了，此时必须以异常结束，不能返回 null 装作成功。
     */
    public static void anyOfSuccessTest2() {
        System.out.println("[anyOfSuccessTest2] 场景：全部副本都失败");
        List<CompletableFuture<String>> futures = new ArrayList<>();
        futures.add(failed(new IllegalStateException("副本A失败")));
        futures.add(failed(new IllegalStateException("副本B失败")));
        checkThrows(IllegalStateException.class, anyOfSuccess(futures)::join, "全部失败时以异常结束");
    }

    /**
     * 场景：空集合边界。
     */
    public static void anyOfSuccessTest3() {
        System.out.println("[anyOfSuccessTest3] 场景：入参为空集合的边界");
        List<CompletableFuture<String>> empty = new ArrayList<>();
        checkThrows(IllegalArgumentException.class, anyOfSuccess(empty)::join,
                "空集合以 IllegalArgumentException 结束，而不是永久不返回");
    }

    /**
     * 场景：手里已经有一批任务（Supplier），想一次性并行提交并按下标拿结果。
     */
    public static void allOfListTest1() throws Exception {
        System.out.println("[allOfListTest1] 场景：批量提交一批任务并按入参顺序收集");
        List<Supplier<Integer>> suppliers = new ArrayList<>();
        suppliers.add(() -> 10);
        suppliers.add(() -> 20);
        suppliers.add(() -> 30);
        checkEquals(Arrays.asList(10, 20, 30), allOfList(suppliers, null).get(),
                "executor 传 null 时走默认池，结果与提交顺序一致");
    }

    /**
     * 场景：两路并行后拼装 —— step1、step2 无依赖可并行，step3 依赖两者的结果。
     */
    public static void thenCombineTest1() throws Exception {
        System.out.println("[thenCombineTest1] 场景：step1 / step2 并行执行，step3 合并两者结果");
        long start = System.currentTimeMillis();
        CompletableFuture<String> step1 = supplyAsync(() -> {
            sleep(200);
            return "step1result";
        });
        CompletableFuture<String> step2 = supplyAsync(() -> {
            sleep(200);
            return "step2result";
        });
        CompletableFuture<String> step3 = thenCombine(step1, step2, (r1, r2) -> r1 + "," + r2 + ",step3result");
        long cost = System.currentTimeMillis() - start;

        checkEquals("step1result,step2result,step3result", step3.get(), "step3 拿到 step1/step2 的结果并合并");
        checkTrue(cost < 400, "step1 与 step2 是并行的，总耗时 " + cost + "ms（串行需 400ms 以上）");
    }

    /**
     * 场景：二路组合中任意一路失败，整体都应该失败。
     */
    public static void thenCombineTest2() {
        System.out.println("[thenCombineTest2] 场景：任一侧失败则整体失败");
        CompletableFuture<String> first = supplyAsync(() -> "ok");
        CompletableFuture<String> second = failed(new IllegalStateException("第二路失败"));
        CompletableFuture<String> combined = thenCombine(first, second, (a, b) -> a + b);
        checkThrows(IllegalStateException.class, combined::join, "整体失败并抛出根因");
    }

    /**
     * 场景（语义边界）：原生 thenCombine 的失败<strong>不短路</strong> —— 一侧已失败，
     * 只要另一侧还在跑，结果 future 就一直不完成。这是最容易被误解的地方。
     */
    public static void thenCombineTest3() {
        System.out.println("[thenCombineTest3] 场景：语义边界——一侧失败不会短路，必须等另一侧结束");
        CompletableFuture<String> fast = new CompletableFuture<>();
        CompletableFuture<String> slow = supplyAsync(() -> {
            sleep(300);
            return "slow";
        });
        CompletableFuture<String> combined = thenCombine(fast, slow, (a, b) -> a + b);

        fast.completeExceptionally(new IllegalStateException("先失败"));
        sleep(80);
        checkTrue(!combined.isDone(),
                "fast 已失败但 slow 还没结束，此时结果 future 仍未完成（失败不短路）");

        checkThrows(IllegalStateException.class, combined::join, "等 slow 也结束后，才抛出异常");
        checkTrue(combined.isDone(), "两侧都结束后结果 future 才进入终态");
    }

    /**
     * 场景：合并逻辑自身抛异常，同样要向外传播，不能静默。
     */
    public static void thenCombineTest4() {
        System.out.println("[thenCombineTest4] 场景：合并函数自身抛异常");
        CompletableFuture<String> combined = thenCombine(supplyAsync(() -> "a"), supplyAsync(() -> "b"),
                (a, b) -> {
                    throw new IllegalStateException("合并逻辑失败");
                });
        checkThrows(IllegalStateException.class, combined::join, "combiner 抛出的异常向外传播");
    }

    /**
     * 场景：组合是"只读"操作 —— 不能污染参与组合的原始 future。
     */
    public static void thenCombineTest5() throws Exception {
        System.out.println("[thenCombineTest5] 场景：原 future 不被污染，且返回的是新的 future");
        CompletableFuture<String> first = supplyAsync(() -> "step1-result");
        CompletableFuture<Integer> second = supplyAsync(() -> 2);
        CompletableFuture<String> combined = thenCombine(first, second, (a, b) -> a + "-" + b);

        checkEquals("step1-result-2", combined.get(), "合并结果正确");
        checkEquals("step1-result", first.get(), "原 first 未被污染，仍可正常取值");
        checkEquals(2, second.get(), "原 second 未被污染，仍可正常取值");
        checkTrue((Object) combined != (Object) first && (Object) combined != (Object) second,
                "返回的是一个全新的 future");
    }

    /**
     * 场景：合并逻辑偏重时，放到业务自管的线程池执行，不占用"恰好完成第二路"的那个线程。
     */
    public static void thenCombineAsyncTest1() throws Exception {
        System.out.println("[thenCombineAsyncTest1] 场景：合并逻辑在指定线程池执行");
        ExecutorService pool = Executors.newFixedThreadPool(2, r -> new Thread(r, "test-combine-pool-"));
        try {
            AtomicReference<String> combineThread = new AtomicReference<>();
            CompletableFuture<String> combined = thenCombineAsync(supplyAsync(() -> "a"), supplyAsync(() -> "b"),
                    (a, b) -> {
                        combineThread.set(Thread.currentThread().getName());
                        return a + b;
                    }, pool);

            checkEquals("ab", combined.get(), "合并结果正确");
            checkTrue(combineThread.get().startsWith("test-combine-pool-"),
                    "合并逻辑在指定池中执行：" + combineThread.get());
        } finally {
            pool.shutdownNow();
        }
    }

    /**
     * 场景：未传线程池时，合并逻辑走工具类的默认池。
     */
    public static void thenCombineAsyncTest2() throws Exception {
        System.out.println("[thenCombineAsyncTest2] 场景：executor 传 null 时合并逻辑走默认池");
        AtomicReference<String> combineThread = new AtomicReference<>();
        CompletableFuture<String> combined = thenCombineAsync(
                CompletableFuture.completedFuture("x"), CompletableFuture.completedFuture("y"),
                (a, b) -> {
                    combineThread.set(Thread.currentThread().getName());
                    return a + b;
                }, null);

        checkEquals("xy", combined.get(), "合并结果正确");
        checkTrue(combineThread.get().startsWith("async-future-"),
                "合并逻辑走默认池：" + combineThread.get());
    }

    /**
     * 场景：fail-fast 版本的主流程 —— 两侧都成功时行为与原生一致。
     */
    public static void thenCombineFailFastTest1() throws Exception {
        System.out.println("[thenCombineFailFastTest1] 场景：两侧都成功时正常合并");
        CompletableFuture<String> combined = thenCombineFailFast(supplyAsync(() -> "step1"),
                supplyAsync(() -> "step2"), (a, b) -> a + "-" + b);
        checkEquals("step1-step2", combined.get(), "两侧都成功时正常合并");
    }

    /**
     * 场景（与原生对比）：一侧已经失败，不想再等另一侧慢下游 —— 接口可以更早失败并降级。
     */
    public static void thenCombineFailFastTest2() throws Exception {
        System.out.println("[thenCombineFailFastTest2] 场景：一侧失败立即结束，不等慢下游（对比原生不短路）");
        CompletableFuture<String> fast = new CompletableFuture<>();
        CompletableFuture<String> slow = supplyAsync(() -> {
            sleep(300);
            return "slow";
        });
        CompletableFuture<String> failFast = thenCombineFailFast(fast, slow, (a, b) -> a + b);
        CompletableFuture<String> origin = thenCombine(fast, slow, (a, b) -> a + b);

        fast.completeExceptionally(new IllegalStateException("快速失败"));
        sleep(80);

        checkTrue(failFast.isDone(), "fail-fast 版本：一侧失败后立即结束，不等 slow");
        checkTrue(!origin.isDone(), "原生 thenCombine 对照组：此时仍未结束，会被慢下游拖住");
        checkThrows(IllegalStateException.class, failFast::join, "fail-fast 抛出先失败的那一侧的异常");
    }

    /**
     * 场景（语义边界）：两侧都成功、但合并函数自己抛异常 —— 结果必须失败，绝不能永不完成。
     */
    public static void thenCombineFailFastTest3() {
        System.out.println("[thenCombineFailFastTest3] 场景：两侧都成功但合并函数抛异常，结果不会永不完成");
        CompletableFuture<String> combined = thenCombineFailFast(supplyAsync(() -> "a"), supplyAsync(() -> "b"),
                (a, b) -> {
                    throw new IllegalStateException("合并逻辑失败");
                });
        checkThrows(IllegalStateException.class, combined::join, "合并函数抛出的异常向外传播");
    }

    /**
     * 场景：两侧都失败时，应以先进入终态的那一侧的异常结束，且不会卡住。
     */
    public static void thenCombineFailFastTest4() {
        System.out.println("[thenCombineFailFastTest4] 场景：两侧都失败");
        CompletableFuture<String> first = failed(new IllegalStateException("第一路失败"));
        CompletableFuture<String> second = failed(new IllegalStateException("第二路失败"));
        CompletableFuture<String> combined = thenCombineFailFast(first, second, (a, b) -> a + b);

        checkTrue(combined.isDone(), "已进入终态，不会卡住");
        checkThrows(IllegalStateException.class, combined::join, "以异常结束");
    }

    // ---------- 6. 批量并行执行 ----------

    /**
     * 场景：批量 IO —— 批量查设备在线状态、批量拉配置，每项处理彼此独立。
     */
    public static void executeTest1() {
        System.out.println("[executeTest1] 场景：批量 IO，按入参维度收集结果");
        Map<String, Integer> result = execute(Arrays.asList("apple", "pear", "apple", null), String::length);
        checkEquals(2, result.size(), "入参去重并忽略 null，最终 2 个 key");
        checkEquals(5, result.get("apple"), "apple 的结果按入参维度收集");
        checkEquals(4, result.get("pear"), "pear 的结果按入参维度收集");
    }

    /**
     * 场景：批量操作的并行度需要与业务其他异步任务隔离（例如批量网关操作不该抢占消息处理线程）。
     */
    public static void executeTest2() throws Exception {
        System.out.println("[executeTest2] 场景：批量操作使用业务自管的线程池");
        ExecutorService pool = Executors.newFixedThreadPool(3, r -> new Thread(r, "test-batch-pool-"));
        try {
            AtomicReference<String> threadName = new AtomicReference<>();
            Map<Integer, String> result = execute(Arrays.asList(1, 2), item -> {
                threadName.set(Thread.currentThread().getName());
                return "item-" + item;
            }, pool);

            checkEquals(2, result.size(), "两个入参都收集到结果");
            checkEquals("item-1", result.get(1), "结果与入参一一对应");
            checkTrue(threadName.get().startsWith("test-batch-pool-"),
                    "执行线程来自指定池：" + threadName.get());
        } finally {
            pool.shutdownNow();
        }
    }

    /**
     * 场景：空集合 / null 入参的边界，应返回空 Map 而不是抛异常。
     */
    public static void executeTest3() {
        System.out.println("[executeTest3] 场景：空集合 / null 入参 / null 任务的边界");
        checkTrue(execute(Collections.<String>emptyList(), String::length).isEmpty(), "空集合返回空 Map");
        checkTrue(execute((Collection<String>) null, String::length).isEmpty(), "null 入参返回空 Map");
        checkTrue(execute(Arrays.asList("a"), null).isEmpty(), "task 为 null 时返回空 Map");
    }

    /**
     * 场景：任务返回 null 表示"这一项没有有效结果"，不应污染收集结果。
     */
    public static void executeTest4() {
        System.out.println("[executeTest4] 场景：任务返回 null 时该项结果不收集");
        Map<Integer, String> result = execute(Arrays.asList(1, 2, 3),
                item -> item == 2 ? null : "item-" + item);
        checkEquals(2, result.size(), "返回 null 的入参不进入结果");
        checkTrue(!result.containsKey(2), "入参 2 无对应结果");
    }

    /**
     * 场景：批量执行中单点失败时的行为 —— 整体抛出，需要容错请在 task 内部自行捕获。
     */
    public static void executeTest5() {
        System.out.println("[executeTest5] 场景：任一任务失败则整体抛出异常");
        checkThrows(IllegalStateException.class,
                () -> execute(Arrays.asList(1, 2), item -> {
                    if (item == 2) {
                        throw new IllegalStateException("item 2 处理失败");
                    }
                    return "item-" + item;
                }),
                "批量执行中单点失败会向上抛出，而不是被静默忽略");
    }

    // ---------- 7. 回调监听 ----------

    /**
     * 场景：回调式 API（thrift / MQ / HTTP）桥接成 CF，之后即可接上 timeout / retry / 聚合。
     */
    public static void toCompletableFutureTest1() throws Exception {
        System.out.println("[toCompletableFutureTest1] 场景：回调式 API 桥接成 CF（成功回调）");
        Callback<String> callback = handler -> new Thread(() -> handler.onSuccess("callback-result")).start();
        CompletableFuture<String> future = toCompletableFuture(callback, null);
        checkEquals("callback-result", future.get(), "回调的 success 结果成为 CF 的结果");
    }

    /**
     * 场景：回调失败 -> CF 进入失败态，可直接接 fallback / retry。
     */
    public static void toCompletableFutureTest2() {
        System.out.println("[toCompletableFutureTest2] 场景：回调式 API 桥接成 CF（失败回调）");
        Callback<String> callback = handler ->
                new Thread(() -> handler.onFailure(new IllegalStateException("回调失败"))).start();
        CompletableFuture<String> future = toCompletableFuture(callback, null);
        checkThrows(IllegalStateException.class, future::join, "回调的 failure 成为 CF 的异常");
    }

    /**
     * 场景：发起调用的那一刻就抛异常（例如连接建立失败），也要转成 CF 的失败态。
     */
    public static void toCompletableFutureTest3() {
        System.out.println("[toCompletableFutureTest3] 场景：发起调用即抛异常");
        Callback<String> callback = handler -> {
            // 只注册观察者，不主动触发，由 asyncCall 决定何时回调
        };
        CompletableFuture<String> future = toCompletableFuture(callback, () -> {
            throw new IllegalStateException("调用即失败");
        });
        checkThrows(IllegalStateException.class, future::join, "invoke 抛出的异常被转成 CF 的异常");
    }

    /**
     * 场景：异步链路收尾 —— 成功时打点计数，且不影响原有结果。
     */
    public static void onCompleteTest1() throws Exception {
        System.out.println("[onCompleteTest1] 场景：成功时打点，不影响原结果");
        AtomicReference<String> traced = new AtomicReference<>();
        CompletableFuture<String> future = onComplete(supplyAsync(() -> "ok"),
                traced::set, e -> traced.set("不该走到这里"));
        checkEquals("ok", future.get(), "原结果不受 onComplete 影响");
        checkEquals("ok", traced.get(), "成功回调被触发");
    }

    /**
     * 场景：失败时告警上报，同样不能把失败"修正"成成功。
     */
    public static void onCompleteTest2() {
        System.out.println("[onCompleteTest2] 场景：失败时告警，且不改变原有结果");
        AtomicReference<String> alarm = new AtomicReference<>();
        CompletableFuture<String> origin = supplyAsync(() -> {
            throw new IllegalStateException("链路失败");
        });
        CompletableFuture<String> future = onComplete(origin, r -> alarm.set("不该走到这里"),
                e -> alarm.set(e.getClass().getSimpleName() + ":" + e.getMessage()));
        checkThrows(IllegalStateException.class, future::join, "onComplete 不改变原结果，链路仍然失败");
        checkEquals("IllegalStateException:链路失败", alarm.get(), "失败回调拿到解包后的业务异常");
    }

    // ---------- 8. 默认线程池托管 ----------

    /**
     * 场景：把内置默认池换成业务自管的池（Spring 启动时注入），用完恢复现场。
     */
    public static void setDefaultExecutorTest1() throws Exception {
        System.out.println("[setDefaultExecutorTest1] 场景：替换默认线程池并在结束后恢复");
        Executor origin = getDefaultExecutor();
        ExecutorService pool = Executors.newSingleThreadExecutor(r -> new Thread(r, "test-default-pool-"));
        try {
            setDefaultExecutor(pool);
            checkTrue(getDefaultExecutor() == pool, "默认池已被替换");

            AtomicReference<String> threadName = new AtomicReference<>();
            checkEquals("via-default", supplyAsync(() -> {
                threadName.set(Thread.currentThread().getName());
                return "via-default";
            }).get(), "不显式传线程池时走新的默认池");
            checkTrue(threadName.get().startsWith("test-default-pool-"),
                    "执行线程来自新默认池：" + threadName.get());
        } finally {
            setDefaultExecutor(origin);
            pool.shutdownNow();
        }
        checkTrue(getDefaultExecutor() == origin, "默认池已恢复原值");
    }

    // ---------- 9. 组合编排与并发控制 ----------

    /**
     * 场景：定时续接的起点 —— 延迟 N 毫秒后再执行下一步。
     */
    public static void delayTest1() {
        System.out.println("[delayTest1] 场景：延迟指定时间后执行续接");
        long start = System.currentTimeMillis();
        String value = delay(150, TimeUnit.MILLISECONDS).thenApply(v -> "after-delay").join();
        long cost = System.currentTimeMillis() - start;

        checkEquals("after-delay", value, "延迟后执行续接并返回结果");
        checkTrue(cost >= 150, "确实等待了 150ms，实测 " + cost + "ms");
    }

    /**
     * 场景：长尾优化 —— 主请求太慢时自动补发备请求，取先成功的那个把 P99 拉下来。
     */
    public static void hedgedRequestTest1() throws Exception {
        System.out.println("[hedgedRequestTest1] 场景：主请求太慢，备请求先成功");
        AtomicInteger backupCalls = new AtomicInteger();
        long start = System.currentTimeMillis();
        String value = hedgedRequest(
                () -> supplyAsync(() -> {
                    sleep(500);
                    return "primary";
                }),
                () -> {
                    backupCalls.incrementAndGet();
                    return supplyAsync(() -> "backup");
                },
                100, TimeUnit.MILLISECONDS).get();
        long cost = System.currentTimeMillis() - start;

        checkEquals("backup", value, "备请求先成功，取备请求结果");
        checkTrue(cost < 400, "在 " + cost + "ms 内返回，没有等满主请求的 500ms");
        checkEquals(1, backupCalls.get(), "备请求被发出了一次");
    }

    /**
     * 场景（对冲窗口）：主请求及时成功时不该产生多余的冗余流量。
     */
    public static void hedgedRequestTest2() throws Exception {
        System.out.println("[hedgedRequestTest2] 场景：主请求先成功，备请求不再发出");
        AtomicInteger backupCalls = new AtomicInteger();
        String value = hedgedRequest(
                () -> supplyAsync(() -> "primary"),
                () -> {
                    backupCalls.incrementAndGet();
                    return supplyAsync(() -> "backup");
                },
                200, TimeUnit.MILLISECONDS).get();

        checkEquals("primary", value, "主请求先成功，取主请求结果");
        sleep(400);
        checkEquals(0, backupCalls.get(), "超过对冲延迟后备请求仍未发出（对冲窗口已被取消）");
    }

    /**
     * 场景：缓存击穿防护 —— 同 key 的并发请求只回源一次，其余共享同一个在途 future。
     */
    public static void singleFlightTest1() throws Exception {
        System.out.println("[singleFlightTest1] 场景：同 key 并发请求共享同一个在途 future，只回源一次");
        AtomicInteger loadCount = new AtomicInteger();
        Supplier<CompletableFuture<String>> loader = () -> {
            loadCount.incrementAndGet();
            return supplyAsync(() -> {
                sleep(100);
                return "loaded";
            });
        };
        CompletableFuture<String> first = singleFlight("test-key-A", loader);
        CompletableFuture<String> second = singleFlight("test-key-A", loader);
        CompletableFuture<String> third = singleFlight("test-key-A", loader);

        checkTrue(first == second && second == third, "并发期间返回的是同一个 future 实例");
        checkEquals("loaded", first.get(), "共享的结果正确");
        checkEquals(1, loadCount.get(), "loader 只被触发了一次");
    }

    /**
     * 场景：任务完成后必须释放 key，否则缓存一旦失效就再也刷新不了。
     */
    public static void singleFlightTest2() throws Exception {
        System.out.println("[singleFlightTest2] 场景：任务完成后 key 被清理，下次调用重新加载");
        AtomicInteger loadCount = new AtomicInteger();
        Supplier<CompletableFuture<String>> loader = () -> {
            loadCount.incrementAndGet();
            return supplyAsync(() -> "loaded");
        };

        checkEquals("loaded", singleFlight("test-key-B", loader).get(), "第一次触发加载");
        sleep(50);
        checkEquals("loaded", singleFlight("test-key-B", loader).get(), "完成后 key 已清理，第二次重新加载");
        checkEquals(2, loadCount.get(), "loader 共执行 2 次");
    }

    /**
     * 场景（失败清理）：加载函数直接抛异常时不能把 key 卡死。
     */
    public static void singleFlightTest3() throws Exception {
        System.out.println("[singleFlightTest3] 场景：加载抛异常后 key 同样被清理");
        Supplier<CompletableFuture<String>> broken = () -> {
            throw new IllegalStateException("加载失败");
        };
        checkThrows(IllegalStateException.class, () -> singleFlight("test-key-C", broken).join(),
                "加载函数直接抛异常时以失败态结束");

        AtomicInteger reloadCount = new AtomicInteger();
        Supplier<CompletableFuture<String>> healthy = () -> {
            reloadCount.incrementAndGet();
            return supplyAsync(() -> "recovered");
        };
        checkEquals("recovered", singleFlight("test-key-C", healthy).get(), "失败后 key 已清理，可以重新加载");
        checkEquals(1, reloadCount.get(), "重新加载确实执行了");
    }

    /**
     * 场景：聚合未超时，行为与 allOf 完全一致。
     */
    public static void allOfWithTimeoutTest1() throws Exception {
        System.out.println("[allOfWithTimeoutTest1] 场景：未超时，行为与 allOf 一致");
        List<CompletableFuture<Integer>> futures = new ArrayList<>();
        futures.add(supplyAsync(() -> 1));
        futures.add(supplyAsync(() -> 2));
        checkEquals(Arrays.asList(1, 2), allOfWithTimeout(futures, 2, TimeUnit.SECONDS, false).get(),
                "未超时正常返回，顺序与入参一致");
    }

    /**
     * 场景：聚合必须给整体设预算，超时即失败（再配合 orDefaultOnTimeout 就是降级）。
     */
    public static void allOfWithTimeoutTest2() {
        System.out.println("[allOfWithTimeoutTest2] 场景：整体超时以 TimeoutException 结束");
        List<CompletableFuture<String>> futures = new ArrayList<>();
        futures.add(supplyAsync(() -> {
            sleep(500);
            return "slow";
        }));
        checkThrows(TimeoutException.class,
                allOfWithTimeout(futures, 100, TimeUnit.MILLISECONDS, false)::join,
                "超过整体预算后以 TimeoutException 结束");
    }

    /**
     * 场景：超时后回收底层任务，避免慢下游继续占着线程资源（前置条件：future 为本次独占创建）。
     */
    public static void allOfWithTimeoutTest3() {
        System.out.println("[allOfWithTimeoutTest3] 场景：超时后取消底层任务（cancelOnTimeout = true）");
        CompletableFuture<String> slow = supplyAsync(() -> {
            sleep(1000);
            return "slow";
        });
        List<CompletableFuture<String>> futures = new ArrayList<>();
        futures.add(slow);

        checkThrows(TimeoutException.class, allOfWithTimeout(futures, 100, TimeUnit.MILLISECONDS, true)::join,
                "超时后以 TimeoutException 结束");
        checkTrue(slow.isCancelled(), "底层任务被取消，不再继续占用线程");
    }

    /**
     * 场景（不能误伤）：失败原因不是超时时，绝不应该取消其他任务。
     */
    public static void allOfWithTimeoutTest4() {
        System.out.println("[allOfWithTimeoutTest4] 场景：非超时的业务失败不误取消其他任务");
        CompletableFuture<String> slow = supplyAsync(() -> {
            sleep(300);
            return "slow";
        });
        List<CompletableFuture<String>> futures = new ArrayList<>();
        futures.add(slow);
        futures.add(failed(new IllegalStateException("某一项失败")));

        checkThrows(IllegalStateException.class, allOfWithTimeout(futures, 3, TimeUnit.SECONDS, true)::join,
                "业务失败照常向上抛出");
        checkTrue(!slow.isCancelled(), "非超时失败不应取消其他任务");
    }

    /**
     * 场景：批量处理上万条数据时用分批提交，把在途任务量和调用方阻塞时间控制在预期内。
     */
    public static void executeBatchedTest1() {
        System.out.println("[executeBatchedTest1] 场景：分批提交大批量任务，结果完整");
        List<Integer> items = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            items.add(i);
        }
        Map<Integer, String> result = executeBatched(items, item -> "item-" + item, 3);

        checkEquals(10, result.size(), "10 个入参全部收集到结果（分 4 批提交）");
        checkEquals("item-0", result.get(0), "首批次结果正确");
        checkEquals("item-7", result.get(7), "中间批次结果正确");
        checkEquals("item-9", result.get(9), "末批次结果正确");
    }

    /**
     * 场景：分批执行的边界 —— 空集合、非法 batchSize、null 任务、入参去重。
     */
    public static void executeBatchedTest2() {
        System.out.println("[executeBatchedTest2] 场景：分批执行的边界");
        checkTrue(executeBatched(Collections.<Integer>emptyList(), i -> i, 3).isEmpty(), "空集合返回空 Map");
        checkEquals(3, executeBatched(Arrays.asList(1, 2, 3), i -> i, 0).size(),
                "batchSize <= 0 时按 1 处理，不影响结果");
        checkTrue(executeBatched(Arrays.asList(1), null, 5).isEmpty(), "task 为 null 时返回空 Map");
        checkEquals(2, executeBatched(Arrays.asList(1, 2, 2, null), i -> i, 2).size(),
                "入参去重并忽略 null，最终 2 个 key");
    }

    // ---------- 10. 组合用法与原生算子对照（ComboTest） ----------
    // 这里全部是"已有方法的搭配"与"刻意不封装的原生算子用法"，不引入任何新 API，
    // 只为把常见组合和原生算子固化成可执行的示例。

    /**
     * 组合：批量并行 + 单项超时降级。
     * <p>
     * 这是批量查询类接口的标准写法 —— 个别慢项单独降级，不能让一项超时把整批拖垮或整批失败。
     */
    public static void orDefaultOnTimeoutComboTest1() {
        System.out.println("[orDefaultOnTimeoutComboTest1] 组合：批量并行 + 单项超时降级");
        List<String> ids = Arrays.asList("fast-1", "slow-2", "fast-3");
        List<CompletableFuture<Boolean>> futures = new ArrayList<>();
        for (String id : ids) {
            futures.add(orDefaultOnTimeout(supplyAsync(() -> {
                if (id.startsWith("slow")) {
                    sleep(300);
                }
                return true;
            }), 100, TimeUnit.MILLISECONDS, false));
        }
        checkEquals(Arrays.asList(true, false, true), allOf(futures).join(),
                "慢项独立降级为 false，其余项不受影响，整批也不会因单项超时而失败");
    }

    /**
     * 组合：聚合合并 + 整体超时 + 降级为空字典。
     * <p>
     * 接口聚合必须给整体设预算，超时就返回降级值，保证接口一定有结果返回。
     */
    public static void allOfMapComboTest1() {
        System.out.println("[allOfMapComboTest1] 组合：allOfMap + 整体超时 + 降级");
        List<CompletableFuture<Map<String, Integer>>> slowFutures = new ArrayList<>();
        slowFutures.add(supplyAsync(() -> {
            sleep(300);
            return Collections.singletonMap("slow", 1);
        }));
        Map<String, Integer> timedOut = orDefaultOnTimeout(allOfMap(slowFutures), 100, TimeUnit.MILLISECONDS,
                Collections.<String, Integer>emptyMap()).join();
        checkTrue(timedOut.isEmpty(), "超过整体预算后降级为空字典，而不是抛异常给调用方");

        List<CompletableFuture<Map<String, Integer>>> quickFutures = new ArrayList<>();
        quickFutures.add(supplyAsync(() -> Collections.singletonMap("a", 1)));
        quickFutures.add(supplyAsync(() -> Collections.singletonMap("b", 2)));
        checkEquals(2, orDefaultOnTimeout(allOfMap(quickFutures), 2, TimeUnit.SECONDS,
                Collections.<String, Integer>emptyMap()).join().size(), "未超时时正常返回合并结果");
    }

    /**
     * 组合：超时降级 + 收尾打点。
     * <p>
     * 这里揭示一个反直觉的点：<b>降级之后整条链路是"成功"的</b>，所以挂在降级之后的
     * 失败回调不会触发。想统计真实的超时/失败次数，必须在降级之前打点。
     */
    public static void onCompleteComboTest1() throws Exception {
        System.out.println("[onCompleteComboTest1] 组合：超时降级 + 收尾打点");
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failureCount = new AtomicInteger();
        CompletableFuture<String> future = onComplete(
                orDefaultOnTimeout(supplyAsync(() -> {
                    sleep(300);
                    return "remote";
                }), 100, TimeUnit.MILLISECONDS, "cache"),
                v -> successCount.incrementAndGet(),
                e -> failureCount.incrementAndGet());

        checkEquals("cache", future.get(), "超时后降级为兜底值");
        checkEquals(1, successCount.get(), "降级之后链路是成功的，成功回调被触发");
        checkEquals(0, failureCount.get(), "失败回调不会触发 —— 想感知超时必须在降级之前打点");
    }

    /**
     * 组合：单项重试 + 部分失败容错降级。
     * <p>
     * 批量场景里"抖动"和"真挂"要区别对待：抖动的靠重试救回来，救不回来的才降级占位。
     */
    public static void allOfSafelyComboTest1() throws Exception {
        System.out.println("[allOfSafelyComboTest1] 组合：单项重试 + 部分失败容错降级");
        AtomicInteger flakyAttempts = new AtomicInteger();
        List<CompletableFuture<String>> futures = new ArrayList<>();
        futures.add(retry(() -> {
            if (flakyAttempts.incrementAndGet() < 2) {
                throw new IllegalStateException("抖动");
            }
            return "flaky-ok";
        }, 2, 10, TimeUnit.MILLISECONDS));
        futures.add(retry(() -> {
            throw new IllegalStateException("始终失败");
        }, 2, 10, TimeUnit.MILLISECONDS));
        futures.add(supplyAsync(() -> "healthy"));

        checkEquals(Arrays.asList("flaky-ok", "unavailable", "healthy"), allOfSafely(futures, "unavailable").join(),
                "抖动项重试救回、真挂项降级占位，整批不失败且顺序与入参一致");
        checkEquals(2, flakyAttempts.get(), "抖动项确实重试了一次才成功");
    }

    /**
     * 组合：重试耗尽后走兜底值。
     * <p>
     * 比单纯"重试到失败就抛异常"更常见的诉求 —— 重试是尽力而为，最终仍要有降级结果。
     */
    public static void fallbackComboTest1() throws Exception {
        System.out.println("[fallbackComboTest1] 组合：重试耗尽后走兜底值");
        AtomicInteger attempts = new AtomicInteger();
        CompletableFuture<String> future = fallback(retry(() -> {
            attempts.incrementAndGet();
            throw new IllegalStateException("下游不可用");
        }, 2, 10, TimeUnit.MILLISECONDS), "degraded");

        checkEquals("degraded", future.get(), "重试耗尽后返回兜底值而不是抛异常");
        checkEquals(3, attempts.get(), "重试策略本身执行了 3 次（1 次首发 + 2 次重试）");
    }

    /**
     * 组合：多机房竞速 + 整体超时 + 降级。
     */
    public static void anyOfSuccessComboTest1() throws Exception {
        System.out.println("[anyOfSuccessComboTest1] 组合：竞速 + 整体超时 + 降级");
        List<CompletableFuture<String>> futures = new ArrayList<>();
        futures.add(supplyAsync(() -> {
            sleep(300);
            return "慢副本";
        }));
        futures.add(failed(new IllegalStateException("快副本失败")));
        futures.add(supplyAsync(() -> "快且成功的副本"));
        checkEquals("快且成功的副本",
                orDefaultOnTimeout(anyOfSuccess(futures), 200, TimeUnit.MILLISECONDS, "fallback").join(),
                "竞速取首个成功结果，整体预算内正常返回");

        List<CompletableFuture<String>> allSlow = new ArrayList<>();
        allSlow.add(supplyAsync(() -> {
            sleep(400);
            return "slow";
        }));
        checkEquals("fallback",
                orDefaultOnTimeout(anyOfSuccess(allSlow), 100, TimeUnit.MILLISECONDS, "fallback").join(),
                "所有副本都慢时整体超时，走降级值");
    }

    /**
     * 组合：二路并行 + 单路超时 + 立即失败。
     * <p>
     * 二路组合里只要一路先超时，就没必要再等另一路 —— 这正是 fail-fast 的价值。
     */
    public static void thenCombineFailFastComboTest1() {
        System.out.println("[thenCombineFailFastComboTest1] 组合：二路并行 + 单路超时 + 立即失败");
        long start = System.currentTimeMillis();
        CompletableFuture<String> detail = thenCombineFailFast(
                supplyAsync(() -> {
                    sleep(1000);
                    return "user";
                }),
                orTimeout(supplyAsync(() -> {
                    sleep(1000);
                    return "order";
                }), 100, TimeUnit.MILLISECONDS),
                (user, order) -> user + "+" + order);

        checkThrows(TimeoutException.class, detail::join, "任一路超时后整体失败");
        long cost = System.currentTimeMillis() - start;
        checkTrue(cost < 500, "fail-fast 立即结束，实测 " + cost + "ms，没有等满另一路的 1000ms");
    }

    /**
     * 组合：回调式 API + 超时降级。
     * <p>
     * thrift / MQ / HTTP 这类"注册回调"的接口本身不保证一定回调，
     * 接上超时能力之后才不会让调用方无限等待。
     */
    public static void toCompletableFutureComboTest1() throws Exception {
        System.out.println("[toCompletableFutureComboTest1] 组合：回调式 API + 超时降级");
        Callback<String> neverCallback = handler -> {
            // 模拟一个迟迟不回调的下游
        };
        checkEquals("cache",
                orDefaultOnTimeout(toCompletableFuture(neverCallback, null), 100, TimeUnit.MILLISECONDS,
                        "cache").join(),
                "下游一直不回调时不再无限等待，超时后走降级");

        Callback<String> normal = handler -> new Thread(() -> handler.onSuccess("callback-ok")).start();
        checkEquals("callback-ok",
                orDefaultOnTimeout(toCompletableFuture(normal, null), 2, TimeUnit.SECONDS, "cache").join(),
                "正常回调时取真实结果");
    }

    // --- 原生算子对照：本类刻意不封装这些算子，此处仅作为用法对照（整合自 CompletableFutureDemo） ---

    /**
     * 原生 {@code thenApply}：上一阶段的结果作为下一阶段的入参，可多级链式串联。
     * <p>
     * 对应 {@code CompletableFutureDemo.testThenApply}。
     */
    public static void thenApplyComboTest1() throws Exception {
        System.out.println("[thenApplyComboTest1] 原生 thenApply：多级链式转换，结果逐级透传");
        AtomicReference<Integer> observed = new AtomicReference<>();
        CompletableFuture<Integer> future = supplyAsync(() -> 1)
                .thenApply(i -> i + 1)
                .thenApply(i -> i << 2)
                .whenComplete((r, e) -> observed.set(r));

        checkEquals(8, future.get(), "1 -> +1 = 2 -> 左移 2 位 = 8，逐级转换结果正确");
        checkEquals(8, observed.get(), "whenComplete 能拿到链末结果，且不影响返回值");
    }

    /**
     * 原生 {@code thenApplyAsync}：转换逻辑不在"完成上一阶段的那个线程"上执行，而是切到指定线程池。
     * <p>
     * 对应 {@code CompletableFutureDemo.testSupplyAsync}。
     */
    public static void thenApplyAsyncComboTest1() throws Exception {
        System.out.println("[thenApplyAsyncComboTest1] 原生 thenApplyAsync：转换逻辑切到指定线程池执行");
        ExecutorService pool = Executors.newSingleThreadExecutor(r -> new Thread(r, "test-apply-async-"));
        try {
            AtomicReference<String> upstreamThread = new AtomicReference<>();
            AtomicReference<String> applyThread = new AtomicReference<>();
            Integer value = supplyAsync(() -> {
                upstreamThread.set(Thread.currentThread().getName());
                return 100 >> 2;
            }).thenApplyAsync(i -> {
                applyThread.set(Thread.currentThread().getName());
                return i * 10;
            }, pool).get();

            checkEquals(250, value, "100 >> 2 = 25，再乘 10 = 250");
            checkTrue(applyThread.get().startsWith("test-apply-async-"),
                    "转换逻辑跑在指定池：" + applyThread.get());
            System.out.println("    (上游线程=" + upstreamThread.get() + "，转换线程=" + applyThread.get() + ")");
        } finally {
            pool.shutdownNow();
        }
    }

    /**
     * 原生 {@code thenAccept}：接收上一阶段的结果并消费，返回 {@code CompletableFuture<Void>}。
     * <p>
     * 对应 {@code CompletableFutureDemo.testThenAccept}。注意原 demo 没有等待完成，
     * 异步输出可能来不及打印 —— 这里补上了 {@code get()}。
     */
    public static void thenAcceptComboTest1() throws Exception {
        System.out.println("[thenAcceptComboTest1] 原生 thenAccept：消费结果，无返回值");
        AtomicReference<String> consumed = new AtomicReference<>();
        CompletableFuture<Void> future = supplyAsync(() -> "consumed-value").thenAccept(consumed::set);

        checkTrue(future.get() == null, "thenAccept 返回 CompletableFuture<Void>，取值为 null");
        checkEquals("consumed-value", consumed.get(), "上一阶段的结果被传入并消费");
    }

    /**
     * 原生 {@code thenRun}：只在上个阶段完成后触发，<b>拿不到</b>上个阶段的结果。
     * <p>
     * 与 {@link #thenAcceptComboTest1()} 正好构成对照：一个没有入参，一个能拿到结果。
     * 对应 {@code CompletableFutureDemo.testThenRun}。
     */
    public static void thenRunComboTest1() throws Exception {
        System.out.println("[thenRunComboTest1] 原生 thenRun：与 thenAccept 对照 —— 拿不到上个阶段的结果");
        AtomicReference<String> accepted = new AtomicReference<>();
        AtomicReference<String> runMarker = new AtomicReference<>("init");

        supplyAsync(() -> "upstream").thenAccept(accepted::set).get();
        supplyAsync(() -> "upstream").thenRun(() -> runMarker.set("ran")).get();

        checkEquals("upstream", accepted.get(), "thenAccept 能把上个阶段的结果消费掉");
        checkEquals("ran", runMarker.get(), "thenRun 会在完成后触发，但它没有入参、拿不到结果");
    }

    /**
     * 原生 {@code whenComplete}：成功和失败<b>都会回调</b>，且<b>不改变</b>链路原有结果；
     * 想把失败转成正常值要用 {@code exceptionally}。
     * <p>
     * 对应 {@code CompletableFutureDemo.testWhenComplete}。
     */
    public static void whenCompleteComboTest1() {
        System.out.println("[whenCompleteComboTest1] 原生 whenComplete：成功失败都回调，且不改变链路结果");
        AtomicInteger calledTimes = new AtomicInteger();
        AtomicReference<Throwable> observed = new AtomicReference<>();

        CompletableFuture<Void> failed = runAsync(() -> {
            throw new IllegalStateException("任务失败");
        }).whenComplete((r, e) -> {
            calledTimes.incrementAndGet();
            observed.set(e);
        });
        checkThrows(IllegalStateException.class, failed::join, "whenComplete 不改变结果，链路仍然是失败态");
        checkEquals(1, calledTimes.get(), "失败时 whenComplete 同样被调用");
        checkTrue(observed.get() != null, "回调里能拿到异常信息：" + observed.get().getMessage());

        runAsync(() -> {
        }).whenComplete((r, e) -> calledTimes.incrementAndGet()).join();
        checkEquals(2, calledTimes.get(), "成功时也会被调用（成功失败走的是同一个回调）");

        CompletableFuture<Void> recovered = runAsync(() -> {
            throw new IllegalStateException("任务失败");
        }).exceptionally(e -> null);
        checkTrue(!recovered.isCompletedExceptionally(), "exceptionally 把失败恢复成正常完成");
    }

    /**
     * 挂载范式：同样面对失败，{@code whenComplete + LogErrorAction} 只记日志、结果不变；
     * {@code handle + DefaultValueHandle} 用默认值兜住、链路由失败变成功。
     * <p>
     * 这是这对组合最容易被混淆的地方 —— 记日志用 whenComplete，要兜底必须用 handle。
     * 对应 {@code CompletableFutureDemo.testThenCombine} 里的挂载写法。
     */
    public static void handleComboTest1() throws Exception {
        System.out.println("[handleComboTest1] 挂载范式：whenComplete 挂日志 vs handle 挂兜底");

        // 注意：lambda 体只有 throw 时推不出 T，必须先赋给带类型的变量，
        // 否则 T 会被推成 Object，后面的 LogErrorAction<String> 就匹配不上了
        CompletableFuture<String> broken = supplyAsync(() -> {
            throw new IllegalStateException("下游失败");
        });
        CompletableFuture<String> logged = broken.whenComplete(
                new LogErrorAction<String>("handleComboTest1.LogErrorAction", "deviceId-1", 100));
        checkThrows(IllegalStateException.class, logged::join,
                "whenComplete 挂 LogErrorAction 只记日志，失败结果原样透传");

        CompletableFuture<String> brokenAgain = supplyAsync(() -> {
            throw new IllegalStateException("下游失败");
        });
        CompletableFuture<String> recovered = brokenAgain.handle(
                new DefaultValueHandle<String>("handleComboTest1.DefaultValueHandle", "degraded-value"));
        checkEquals("degraded-value", recovered.get(), "handle 挂 DefaultValueHandle 用默认值兜住，链路变成功");
        checkTrue(!recovered.isCompletedExceptionally(), "链路已经不再是失败态");
    }

    // ==================== 测试入口 ====================

    public static void main(String[] args) throws Exception {
        System.out.println("========== 1. 基础异步 ==========");
        supplyAsyncTest1();
        supplyAsyncTest2();
        runAsyncTest1();
        failedTest1();

        System.out.println("\n========== 2. 超时控制 ==========");
        orTimeoutTest1();
        orTimeoutTest2();
        orDefaultOnTimeoutTest1();
        orDefaultOnTimeoutTest2();
        orFallbackOnTimeoutTest1();

        System.out.println("\n========== 3. 异常兜底与解包 ==========");
        unwrapTest1();
        unwrapTest2();
        fallbackTest1();
        fallbackIfNullTest1();
        fallbackIfNullTest2();

        System.out.println("\n========== 4. 失败重试 ==========");
        retryTest1();
        retryTest2();
        retryTest3();
        retryTest4();
        retryAsyncTest1();
        retryAsyncTest2();
        retryAsyncTest3();
        retryAsyncTest4();

        System.out.println("\n========== 5. 聚合收敛 ==========");
        allOfTest1();
        allOfTest2();
        allOfTest3();
        allOfSafelyTest1();
        allOfMapTest1();
        anyOfTest1();
        anyOfSuccessTest1();
        anyOfSuccessTest2();
        anyOfSuccessTest3();
        allOfListTest1();
        thenCombineTest1();
        thenCombineTest2();
        thenCombineTest3();
        thenCombineTest4();
        thenCombineTest5();
        thenCombineAsyncTest1();
        thenCombineAsyncTest2();
        thenCombineFailFastTest1();
        thenCombineFailFastTest2();
        thenCombineFailFastTest3();
        thenCombineFailFastTest4();

        System.out.println("\n========== 6. 批量并行执行 ==========");
        executeTest1();
        executeTest2();
        executeTest3();
        executeTest4();
        executeTest5();

        System.out.println("\n========== 7. 回调监听 ==========");
        toCompletableFutureTest1();
        toCompletableFutureTest2();
        toCompletableFutureTest3();
        onCompleteTest1();
        onCompleteTest2();

        System.out.println("\n========== 8. 默认线程池托管 ==========");
        setDefaultExecutorTest1();

        System.out.println("\n========== 9. 组合编排与并发控制 ==========");
        delayTest1();
        hedgedRequestTest1();
        hedgedRequestTest2();
        singleFlightTest1();
        singleFlightTest2();
        singleFlightTest3();
        allOfWithTimeoutTest1();
        allOfWithTimeoutTest2();
        allOfWithTimeoutTest3();
        allOfWithTimeoutTest4();
        executeBatchedTest1();
        executeBatchedTest2();

        System.out.println("\n========== 10. 组合用法与原生算子对照 ==========");
        orDefaultOnTimeoutComboTest1();
        allOfMapComboTest1();
        onCompleteComboTest1();
        allOfSafelyComboTest1();
        fallbackComboTest1();
        anyOfSuccessComboTest1();
        thenCombineFailFastComboTest1();
        toCompletableFutureComboTest1();
        thenApplyComboTest1();
        thenApplyAsyncComboTest1();
        thenAcceptComboTest1();
        thenRunComboTest1();
        whenCompleteComboTest1();
        handleComboTest1();

        System.out.println("\n全部用例通过");
    }

    // ==================== 断言工具 ====================

    /**
     * 断言相等，不满足则抛 AssertionError
     */
    private static void checkEquals(Object expected, Object actual, String scene) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("【断言失败】" + scene + " 期望=" + expected + " 实际=" + actual);
        }
        System.out.println("  [OK] " + scene + " -> " + actual);
    }

    /**
     * 断言条件为真，不满足则抛 AssertionError
     */
    private static void checkTrue(boolean condition, String scene) {
        if (!condition) {
            throw new AssertionError("【断言失败】" + scene);
        }
        System.out.println("  [OK] " + scene);
    }

    /**
     * 断言执行后抛出指定类型的异常（沿 cause 链查找），不满足则抛 AssertionError
     */
    private static void checkThrows(Class<? extends Throwable> expectedType, Supplier<?> action, String scene) {
        Throwable caught = null;
        Object result = null;
        try {
            result = action.get();
        } catch (Throwable throwable) {
            caught = throwable;
        }
        if (caught == null) {
            throw new AssertionError("【断言失败】" + scene
                    + " 期望抛出 " + expectedType.getSimpleName() + "，实际正常返回：" + result);
        }
        Throwable matched = findThrowable(caught, expectedType);
        if (matched == null) {
            throw new AssertionError("【断言失败】" + scene + " 期望抛出 " + expectedType.getSimpleName()
                    + "，实际抛出：" + caught.getClass().getSimpleName() + " - " + caught.getMessage(), caught);
        }
        System.out.println("  [OK] " + scene + " -> " + matched.getClass().getSimpleName() + ": " + matched.getMessage());
    }

    /**
     * 沿 cause 链查找指定类型的异常
     */
    private static Throwable findThrowable(Throwable throwable, Class<? extends Throwable> type) {
        Throwable current = throwable;
        while (current != null) {
            if (type.isInstance(current)) {
                return current;
            }
            if (current == current.getCause()) {
                return null;
            }
            current = current.getCause();
        }
        return null;
    }

    private static void sleep(long millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
