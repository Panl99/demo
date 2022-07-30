package com.lp.demo.action.designpatterns_in_action.visitorpattern;

/**
 * @author lp
 * @date 2022/7/30 23:05
 * @desc 6.使用访问者模式
 **/
public class Main {
    public static void main(String[] args) {
        Element element = new ConcreteElementA();
        element.accept(new ConcreteVisitorA());
        element.accept(new ConcreteVisitorB());
    }
}
