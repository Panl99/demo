package com.lp.demo.spring.strategy;

import com.lp.demo.spring.strategy.common.StrategyService;

public abstract class StrategyService_A extends StrategyService {

    @Override
    public void invoke(String s) {
        throw new UnsupportedOperationException();
    }

    public void invokeA(String s) {
        throw new UnsupportedOperationException();
    }
}
