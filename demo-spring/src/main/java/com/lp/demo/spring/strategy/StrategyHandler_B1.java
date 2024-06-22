package com.lp.demo.spring.strategy;

import com.lp.demo.spring.strategy.common.StrategyFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class StrategyHandler_B1 extends StrategyService_B {

    @Resource
    StrategyFactory<StrategyTypeEnum_B, StrategyService_B> strategyFactory;

    @Override
    public void invoke(String s) {
        System.out.println("StrategyHandler_B1: s = " + s);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        strategyFactory.register(StrategyTypeEnum_B.B_1, this);
    }

}
