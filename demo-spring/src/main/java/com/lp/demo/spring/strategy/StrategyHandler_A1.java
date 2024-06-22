package com.lp.demo.spring.strategy;

import com.lp.demo.spring.strategy.common.StrategyFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class StrategyHandler_A1 extends StrategyService_A {

    @Resource
    StrategyFactory<StrategyTypeEnum_A, StrategyService_A> strategyFactory;

    @Override
    public void invoke(String s) {
        System.out.println("StrategyHandler_A1.invoke: s = " + s);
    }

    @Override
    public void invokeA(String s) {
        System.out.println("StrategyHandler_A1.invokeA: s = " + s);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        strategyFactory.register(StrategyTypeEnum_A.A_1, this);
    }

}
