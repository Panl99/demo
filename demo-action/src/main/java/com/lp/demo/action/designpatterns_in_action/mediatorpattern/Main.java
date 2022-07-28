package com.lp.demo.action.designpatterns_in_action.mediatorpattern;

/**
 * @author lp
 * @date 2022/7/28 22:19
 * @desc 6.使用中介者模式
 **/
public class Main {
    public static void main(String[] args) {
        // 1.创建一个具体的中介者
        ConcreteMediator mediator = new ConcreteMediator();
        // 2.定义具体同事类，让两个具体同事类认识中介者对象
        ConcreteColleagueA colleagueA = new ConcreteColleagueA(mediator);
        ConcreteColleagueB colleagueB = new ConcreteColleagueB(mediator);
        // 3.让中介者认识具体同事类对象
        mediator.setColleagueA(colleagueA);
        mediator.setColleagueB(colleagueB);
        // 4.同事类通过中介者发送消息
        colleagueA.operation("I'm colleagueA!");
        colleagueB.operation("I'm colleagueB!");
    }
}
