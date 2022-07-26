package com.lp.demo.action.designpatterns_in_action.bridgepattern;

/**
 * @author lp
 * @date 2022/7/26 22:33
 * @desc 3.定义具体实现者B
 **/
public class ConcreteImplementorB implements Implementor {
    @Override
    public void operation() {
        System.out.println("ConcreteImplementorB.operation()...");
    }
}
