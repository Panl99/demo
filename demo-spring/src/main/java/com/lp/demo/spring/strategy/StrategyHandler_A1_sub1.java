package com.lp.demo.spring.strategy;

import org.springframework.stereotype.Component;

@Component
public class StrategyHandler_A1_sub1 extends StrategyHandler_A1 {

    @Override
    public void invoke(String s) {
        super.invoke(s + 1);
        System.out.println("StrategyHandler_A1_sub1.invoke: s = " + s);
    }

}