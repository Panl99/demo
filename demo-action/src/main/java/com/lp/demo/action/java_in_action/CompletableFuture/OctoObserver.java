package com.lp.demo.action.java_in_action.CompletableFuture;

/**
 * @author lp
 * @date 2025/7/16 12:09
 * @desc
 **/
public interface OctoObserver<T> {

    void onSuccess(T t);

    void onFailure(Throwable throwable);

}
