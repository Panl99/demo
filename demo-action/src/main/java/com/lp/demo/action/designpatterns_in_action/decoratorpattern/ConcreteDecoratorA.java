package com.lp.demo.action.designpatterns_in_action.decoratorpattern;

/**
 * @author lp
 * @date 2022/7/23 15:39
 * @desc 4.定义具体装饰类A，添加具体的功能
 **/
public class ConcreteDecoratorA extends Decorator {

    private String addedState;

    @Override
    public void operation() {
        // 首先运行Component的operation()方法
        super.operation();
        // 再执行本类的功能，对Component进行装饰
        addedState = "new state!";
        System.out.println("ConcreteDecoratorA.operation..." + addedState);
    }
}
