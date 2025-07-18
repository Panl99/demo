package com.lp.demo.action.java_in_action.CompletableFuture;

import com.lp.demo.common.exception.DisplayableException;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.async.AsyncMethodCallback;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * CompletableFuture封装工具类
 */
@Slf4j
public class FutureUtils {
    /**
     * 该方法为美团内部rpc注册监听的封装，可以作为其他实现的参照
     * OctoThriftCallback 为thrift回调方法
     * ThriftAsyncCall 为自定义函数，用来表示一次thrift调用（定义如上）
     */
    public static <T> CompletableFuture<T> toCompletableFuture(final OctoThriftCallback<?, T> callback,
                                                               ThriftAsyncCall thriftCall) {
        CompletableFuture<T> thriftResultFuture = new CompletableFuture<>();
        callback.addObserver(new OctoObserver<T>() {
            @Override
            public void onSuccess(T t) {
                thriftResultFuture.complete(t);
            }

            @Override
            public void onFailure(Throwable throwable) {
                thriftResultFuture.completeExceptionally(throwable);
            }
        });
        if (thriftCall != null) {
            try {
                thriftCall.invoke();
            } catch (DisplayableException e) {
                thriftResultFuture.completeExceptionally(e);
            }
        }
        return thriftResultFuture;
    }

    /**
     * 设置CF状态为失败
     */
    public static <T> CompletableFuture<T> failed(Throwable ex) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        completableFuture.completeExceptionally(ex);
        return completableFuture;
    }

    /**
     * 设置CF状态为成功
     */
    public static <T> CompletableFuture<T> success(T result) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        completableFuture.complete(result);
        return completableFuture;
    }

    /**
     * 将List<CompletableFuture<T>> 转为 CompletableFuture<List<T>>
     */
    public static <T> CompletableFuture<List<T>> sequence(Collection<CompletableFuture<T>> completableFutures) {
        return CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture<?>[0]))
                .thenApply(v -> completableFutures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList())
                );
    }

    /**
     * 将List<CompletableFuture<List<T>>> 转为 CompletableFuture<List<T>>
     * 多用于分页查询的场景
     */
    public static <T> CompletableFuture<List<T>> sequenceList(Collection<CompletableFuture<List<T>>> completableFutures) {
        return CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture<?>[0]))
                .thenApply(v -> completableFutures.stream()
                        .flatMap(listFuture -> listFuture.join().stream())
                        .collect(Collectors.toList()));
    }

    /**
     * 将List<CompletableFuture<Map<K, V>>> 转为 CompletableFuture<Map<K, V>>
     * @param mergeFunction 自定义key冲突时的merge策略
     */
    public static <K, V> CompletableFuture<Map<K, V>> sequenceMap(Collection<CompletableFuture<Map<K, V>>> completableFutures,
                                                                  BinaryOperator<V> mergeFunction) {
        return CompletableFuture
                .allOf(completableFutures.toArray(new CompletableFuture<?>[0]))
                .thenApply(v -> completableFutures.stream().map(CompletableFuture::join)
                        .flatMap(map -> map.entrySet().stream())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, mergeFunction)));
    }

    /**
     * 将List<CompletableFuture<T>> 转为 CompletableFuture<List<T>>，并过滤调null值
     */
    public static <T> CompletableFuture<List<T>> sequenceNonNull(Collection<CompletableFuture<T>> completableFutures) {
        return CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture<?>[0]))
                .thenApply(v -> completableFutures.stream()
                        .map(CompletableFuture::join)
                        .filter(e -> e != null)
                        .collect(Collectors.toList()));
    }

    /**
     * 将List<CompletableFuture<List<T>>> 转为 CompletableFuture<List<T>>，并过滤掉null值
     * 多用于分页查询的场景
     */
    public static <T> CompletableFuture<List<T>> sequenceListNonNull(Collection<CompletableFuture<List<T>>> completableFutures) {
        return CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture<?>[0]))
                .thenApply(v -> completableFutures.stream()
                        .flatMap(listFuture -> listFuture.join().stream().filter(e -> e != null))
                        .collect(Collectors.toList()));
    }

    /**
     * 将List<CompletableFuture<Map<K, V>>> 转为 CompletableFuture<Map<K, V>>
     * @param filterFunction 自定义过滤策略
     */
    public static <T> CompletableFuture<List<T>> sequence(Collection<CompletableFuture<T>> completableFutures,
                                                          Predicate<? super T> filterFunction) {
        return CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture<?>[0]))
                .thenApply(v -> completableFutures.stream()
                        .map(CompletableFuture::join)
                        .filter(filterFunction)
                        .collect(Collectors.toList()));
    }

    /**
     * 将List<CompletableFuture<List<T>>> 转为 CompletableFuture<List<T>>
     * @param filterFunction 自定义过滤策略
     */
    public static <T> CompletableFuture<List<T>> sequenceList(Collection<CompletableFuture<List<T>>> completableFutures,
                                                              Predicate<? super T> filterFunction) {
        return CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture<?>[0]))
                .thenApply(v -> completableFutures.stream()
                        .flatMap(listFuture -> listFuture.join().stream().filter(filterFunction))
                        .collect(Collectors.toList()));
    }

    /**
     * 将CompletableFuture<Map<K,V>>的list转为 CompletableFuture<Map<K,V>>。 多个map合并为一个map。 如果key冲突，采用新的value覆盖。
     */
    public static <K, V> CompletableFuture<Map<K, V>> sequenceMap(Collection<CompletableFuture<Map<K, V>>> completableFutures) {
        return CompletableFuture
                .allOf(completableFutures.toArray(new CompletableFuture<?>[0]))
                .thenApply(v -> completableFutures.stream().map(CompletableFuture::join)
                        .flatMap(map -> map.entrySet().stream())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b)));
    }
}