package com.lp.demo.action.designpatterns_in_action.strategypattern;

import java.util.function.Supplier;

/**
 * @author lp
 * @date 2022/7/23 13:46
 * @desc 5.结合简单工厂模式，枚举具体策略对象
 **/
public enum ConcreteStrategyEnum {

    /**
     * 具体策略A
     */
    CONCRETE_STRATEGY_A(ConcreteStrategyA::new),
    /**
     * 具体策略B
     */
    CONCRETE_STRATEGY_B(ConcreteStrategyB::new);


    private final Supplier<AbstractStrategy> strategy;

    ConcreteStrategyEnum(Supplier<AbstractStrategy> strategy) {
        this.strategy = strategy;
    }

    public Supplier<AbstractStrategy> getStrategy() {
        return strategy;
    }
}
