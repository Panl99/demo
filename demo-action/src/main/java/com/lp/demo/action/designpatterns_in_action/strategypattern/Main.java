package com.lp.demo.action.designpatterns_in_action.strategypattern;

/**
 * @author lp
 * @date 2022/7/23 13:27
 * @desc 6.使用策略模式
 **/
public class Main {
    public static void main(String[] args) {
        ConcreteStrategyContext context;

        /**
         * 执行策略A
         */
        context = new ConcreteStrategyContext(new ConcreteStrategyA());
        context.executeFunction1();
        context.executeFunction2();

        /**
         * 结合模板模式，执行策略B
         */
        context = new ConcreteStrategyContext(new ConcreteStrategyB());
        context.execute();

        /**
         * 结合工厂模式、模板方法、策略模式，执行具体策略A
         */
        context = new ConcreteStrategyContext(ConcreteStrategyEnum.CONCRETE_STRATEGY_A);
        context.execute();

    }
}
