package com.lp.demo.action.designpatterns_in_action.mediatorpattern;

/**
 * @author lp
 * @date 2022/7/28 22:20
 * @desc 1.定义抽象中介者
 **/
public abstract class Mediator {
    /**
     * 发送消息
     *
     * @param message   消息
     * @param colleague 具体同事类
     */
    public abstract void send(String message, Colleague colleague);
}
