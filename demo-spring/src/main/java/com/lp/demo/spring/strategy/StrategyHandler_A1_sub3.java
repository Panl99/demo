package com.lp.demo.spring.strategy;

import org.springframework.stereotype.Component;

@Component
public class StrategyHandler_A1_sub3 extends StrategyHandler_A1 {

    @Override
    public void invoke(String s) {
        System.out.println("StrategyHandler_A1_sub3.invoke: s = " + s);
    }

}
