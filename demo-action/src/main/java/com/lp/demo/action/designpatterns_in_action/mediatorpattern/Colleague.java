package com.lp.demo.action.designpatterns_in_action.mediatorpattern;

/**
 * @author lp
 * @date 2022/7/28 22:24
 * @desc 2.定义抽象同事类
 **/
public abstract class Colleague {
    // 持有中介者实例
    protected Mediator mediator;

    public Colleague(Mediator mediator) {
        this.mediator = mediator;
    }

    //    public void setMediator(Mediator mediator) {
//        this.mediator = mediator;
//    }

    public abstract void operation(String message);
}
