package com.lp.demo.action.designpatterns_in_action.strategypattern;


/**
 * @author lp
 * @date 2022/7/23 13:21
 * @desc 4.维护对具体策略对象的引用
 **/
public class ConcreteStrategyContext {

    AbstractStrategy strategy;

    public ConcreteStrategyContext(AbstractStrategy strategy) {
        this.strategy = strategy;
    }

    public void executeFunction1() {
        strategy.function1();
    }

    public void executeFunction2() {
        strategy.function2();
    }

    /**
     * 结合模板方法，具体步骤执行顺序
     */
    public void execute() {
        strategy.function1();
        strategy.function2();
    }

    /**
     * 结合简单工厂模式，创建具体策略对象
     * 客户端调用只需要认识ConcreteStrategyContext类，不需要认识工厂类，降低耦合
     */
    public ConcreteStrategyContext(ConcreteStrategyEnum strategy) {
        this.strategy = strategy.getStrategy().get();
    }
}
