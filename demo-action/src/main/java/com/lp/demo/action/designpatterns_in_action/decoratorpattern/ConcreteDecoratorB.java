package com.lp.demo.action.designpatterns_in_action.decoratorpattern;

/**
 * @author lp
 * @date 2022/7/23 15:39
 * @desc 5.定义具体装饰类B，添加具体的功能
 **/
public class ConcreteDecoratorB extends Decorator {

    @Override
    public void operation() {
        // 首先运行Component的operation()方法
        super.operation();
        // 再执行本类的功能
        addedBehavior();
        System.out.println("ConcreteDecoratorB.operation...");
    }

    /**
     * 添加特定行为，对Component进行装饰
     */
    private void addedBehavior() {
        System.out.println("ConcreteDecoratorB.addedBehavior...");
    }
}
