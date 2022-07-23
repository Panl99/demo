package com.lp.demo.action.designpatterns_in_action.strategypattern;

/**
 * @author lp
 * @date 2022/7/23 13:13
 * @desc 2.具体策略A的实现
 **/
public class ConcreteStrategyA extends AbstractStrategy {

    @Override
    public void function1() {
        System.out.println("ConcreteStrategyA.function1...");
    }

    @Override
    public void function2() {
        System.out.println("ConcreteStrategyA.function2...");
    }
}
