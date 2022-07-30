package com.lp.demo.action.designpatterns_in_action.visitorpattern;

/**
 * @author lp
 * @date 2022/7/30 23:16
 * @desc 5.定义具体访问者B
 **/
public class ConcreteVisitorB implements Visitor {
    /**
     * 访问者B 访问元素A
     * @param elementA
     */
    @Override
    public void visit(ConcreteElementA elementA) {
        System.out.println("ConcreteVisitorB.visitA()...");
        elementA.operationA();
    }
}
