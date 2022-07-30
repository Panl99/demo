package com.lp.demo.action.designpatterns_in_action.visitorpattern;

/**
 * @author lp
 * @date 2022/7/30 23:16
 * @desc 4.定义具体访问者A
 **/
public class ConcreteVisitorA implements Visitor {
    /**
     * 访问者A 访问元素A
     * @param elementA
     */
    @Override
    public void visit(ConcreteElementA elementA) {
        System.out.println("ConcreteVisitorA.visitA()...");
        elementA.operationA();
    }
}
