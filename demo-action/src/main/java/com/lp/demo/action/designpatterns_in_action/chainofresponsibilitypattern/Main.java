package com.lp.demo.action.designpatterns_in_action.chainofresponsibilitypattern;

/**
 * @author lp
 * @date 2022/7/27 21:34
 * @desc 6.使用责任链模式
 **/
public class Main {
    public static void main(String[] args) {
        // 1.定义责任链的处理者
        ConcreteHandlerA handlerA = new ConcreteHandlerA("handlerA");
        ConcreteHandlerB handlerB = new ConcreteHandlerB("handlerB");
        ConcreteHandlerC handlerC = new ConcreteHandlerC("handlerC");
        // 2.将责任链上各个处理者连接起来
        handlerA.setHandler(handlerB);
        handlerB.setHandler(handlerC);
        // 3.调用责任链上第一个处理者
        handlerA.operator();
    }
}
