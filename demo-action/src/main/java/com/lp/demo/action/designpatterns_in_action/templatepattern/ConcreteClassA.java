package com.lp.demo.action.designpatterns_in_action.templatepattern;

/**
 * @author lp
 * @date 2022/7/21 22:53
 * @desc 具体类A的实现
 **/
public class ConcreteClassA extends AbstractClass {
    @Override
    public void step1() {
        System.out.println("ConcreteClassA.step1...");
    }

    @Override
    public void step2() {
        System.out.println("ConcreteClassA.step2...");
    }
}
