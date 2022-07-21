package com.lp.demo.action.designpatterns_in_action.templatepattern;

/**
 * @author lp
 * @date 2022/7/21 22:54
 * @desc 具体类B的实现
 **/
public class ConcreteClassB extends AbstractClass {
    @Override
    public void step1() {
        System.out.println("ConcreteClassB.step1...");
    }

    @Override
    public void step2() {
        System.out.println("ConcreteClassB.step2...");
    }
}
