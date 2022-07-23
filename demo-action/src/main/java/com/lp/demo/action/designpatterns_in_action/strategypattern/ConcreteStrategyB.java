package com.lp.demo.action.designpatterns_in_action.strategypattern;

/**
 * @author lp
 * @date 2022/7/23 13:13
 * @desc 3.具体策略B的实现
 **/
public class ConcreteStrategyB extends AbstractStrategy {

    @Override
    public void function1() {
        System.out.println("ConcreteStrategyB.function1...");
    }

    @Override
    public void function2() {
        System.out.println("ConcreteStrategyB.function2...");
    }
}
