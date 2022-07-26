package com.lp.demo.action.designpatterns_in_action.bridgepattern;

/**
 * @author lp
 * @date 2022/7/26 22:36
 * @desc 4.定义抽象类，注入Implementor，实现桥接模式
 **/
public abstract class ImplementorBridge {

    private Implementor implementor;

    public Implementor getImplementor() {
        return implementor;
    }

    public void setImplementor(Implementor implementor) {
        this.implementor = implementor;
    }

    public void execute() {
        this.implementor.operation();
    }
}
