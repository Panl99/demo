package com.lp.demo.action.java_in_action.CompletableFuture;

/**
 * @author lp
 * @date 2025/7/16 12:11
 * @desc
 **/
public interface OctoThriftCallback<R, T> {

    void addObserver(OctoObserver<T> observer);
}
