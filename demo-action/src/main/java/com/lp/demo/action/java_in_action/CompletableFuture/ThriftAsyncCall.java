package com.lp.demo.action.java_in_action.CompletableFuture;

import com.lp.demo.common.exception.DisplayableException;

@FunctionalInterface
public interface ThriftAsyncCall {
    void invoke() throws DisplayableException;
}