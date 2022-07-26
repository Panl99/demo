package com.lp.demo.action.designpatterns_in_action.bridgepattern;

/**
 * @author lp
 * @date 2022/7/26 22:33
 * @desc 2.定义具体实现者A
 **/
public class ConcreteImplementorA implements Implementor {
    @Override
    public void operation() {
        System.out.println("ConcreteImplementorA.operation()...");
    }
}
