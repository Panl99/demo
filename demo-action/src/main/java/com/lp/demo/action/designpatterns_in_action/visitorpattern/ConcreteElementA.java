package com.lp.demo.action.designpatterns_in_action.visitorpattern;

/**
 * @author lp
 * @date 2022/7/30 23:12
 * @desc 3.定义具体元素A
 **/
public class ConcreteElementA implements Element {

    @Override
    public void accept(Visitor visitor) {
        System.out.println("ConcreteElementA.accept()...");
        visitor.visit(this);
    }

    public void operationA() {
        System.out.println("ConcreteElementA.operationA()...");
    }
}
