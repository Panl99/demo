package com.lp.demo.action.designpatterns_in_action.chainofresponsibilitypattern;

/**
 * @author lp
 * @date 2022/7/27 21:47
 * @desc 3.定义抽象处理器类
 *         将责任链上的各个具体处理者连接起来
 **/
public abstract class AbstractHandler {
    private Handler handler;

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }
}
