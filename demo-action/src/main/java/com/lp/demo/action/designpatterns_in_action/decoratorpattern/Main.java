package com.lp.demo.action.designpatterns_in_action.decoratorpattern;

/**
 * @author lp
 * @date 2022/7/23 15:51
 * @desc 6.使用装饰者模式
 **/
public class Main {

    public static void main(String[] args) {

        ConcreteComponent concreteComponent = new ConcreteComponent();
        ConcreteDecoratorA decoratorA = new ConcreteDecoratorA();
        ConcreteDecoratorB decoratorB = new ConcreteDecoratorB();

        // 装饰者A对象 包装 具体实现对象
        decoratorA.setComponent(concreteComponent);
        // 装饰者B对象 包装 装饰着A对象
        decoratorB.setComponent(decoratorA);
        // 执行装饰者B
        decoratorB.operation();

        /*
         * 如果只有具体类ConcreteComponent，没有抽象的Component类：
         * - 那么Decorator类可以是ConcreteComponent的子类。
         *
         * 如果只有一个具体装饰者类ConcreteDecoratorA：
         * - 那么可以不要Decorator类，可以把Decorator 和 ConcreteDecoratorA的职责合并为一个类。
         */
    }

}
