package com.lp.demo.action.designpatterns_in_action.chainofresponsibilitypattern;

/**
 * @author lp
 * @date 2022/7/27 21:49
 * @desc 3.定义具体处理者B
 **/
public class ConcreteHandlerB extends AbstractHandler implements Handler {
    private String name;

    public ConcreteHandlerB(String name) {
        this.name = name;
    }

    @Override
    public void operator() {
        System.out.println("ConcreteHandlerB.operator()..." + name);
        // 获取下一个处理者并调用
        if (getHandler() != null) {
            getHandler().operator();
        }
    }
}
