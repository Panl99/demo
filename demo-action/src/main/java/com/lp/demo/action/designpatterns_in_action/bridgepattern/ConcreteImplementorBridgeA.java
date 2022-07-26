package com.lp.demo.action.designpatterns_in_action.bridgepattern;

/**
 * @author lp
 * @date 2022/7/26 22:40
 * @desc 5.具体桥接实现者A
 *         用于实现自定义功能，也可以直接使用父类ImplementorBridge提供的功能
 **/
public class ConcreteImplementorBridgeA extends ImplementorBridge {

    @Override
    public void execute() {
        getImplementor().operation();
    }

    public void test() {
        System.out.println("ConcreteImplementorBridgeA.test()...");
    }
}
