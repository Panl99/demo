package com.lp.demo.action.designpatterns_in_action.mediatorpattern;

/**
 * @author lp
 * @date 2022/7/28 22:35
 * @desc 5.定义具体中介者
 **/
public class ConcreteMediator extends Mediator {

    // 持有同事类实例
    protected ConcreteColleagueA colleagueA;
    protected ConcreteColleagueB colleagueB;

    public void setColleagueA(ConcreteColleagueA colleagueA) {
        this.colleagueA = colleagueA;
    }

    public void setColleagueB(ConcreteColleagueB colleagueB) {
        this.colleagueB = colleagueB;
    }

    /**
     * 具体中介者完成消息的发送
     *
     * @param message   消息
     * @param colleague 具体同事类
     * @return
     */
    @Override
    public void send(String message, Colleague colleague) {
        if (colleague == colleagueA) {
            colleagueB.action(message);
        } else {
            colleagueA.action(message);
        }
    }
}
