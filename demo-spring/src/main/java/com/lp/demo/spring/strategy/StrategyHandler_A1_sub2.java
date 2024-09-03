package com.lp.demo.spring.strategy;

import org.springframework.stereotype.Component;

@Component
public class StrategyHandler_A1_sub2 extends StrategyHandler_A1 {

    @Override
    public void invoke(String s) {
        System.out.println("StrategyHandler_A1_sub2.invoke: s = " + s);
    }

}
