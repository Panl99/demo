package com.lp.demo.spring.strategy;

import com.lp.demo.spring.strategy.common.StrategyFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class StrategyHandler_A2_3 extends StrategyService_A {

    @Resource
    StrategyFactory<StrategyTypeEnum_A, StrategyService_A> strategyFactory;

    @Override
    public void invoke(String s) {
        System.out.println("StrategyHandler_A2_3.invoke: s = " + s);
    }

    @Override
    public void invokeA(String s) {
        System.out.println("StrategyHandler_A2_3: s = " + s);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        strategyFactory.register(StrategyTypeEnum_A.A_2, this);
        strategyFactory.register(StrategyTypeEnum_A.A_3, this);
    }

}
