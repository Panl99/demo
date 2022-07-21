package com.lp.demo.action.designpatterns_in_action.templatepattern;

/**
 * @author lp
 * @date 2022/7/21 23:03
 * @desc
 **/
public class Main {
    public static void main(String[] args) {
        AbstractClass ac;

        ac = new ConcreteClassA();
        ac.execute();

        ac = new ConcreteClassB();
        ac.execute();
    }
}
