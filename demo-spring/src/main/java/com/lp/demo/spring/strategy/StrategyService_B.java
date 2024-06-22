package com.lp.demo.spring.strategy;

import com.lp.demo.spring.strategy.common.StrategyService;

public abstract class StrategyService_B extends StrategyService {

    @Override
    public void invoke(String s) {
        throw new UnsupportedOperationException();
    }

}
