package com.lp.demo.action.designpatterns_in_action.mediatorpattern;

/**
 * @author lp
 * @date 2022/7/28 22:31
 * @desc 3.定义具体同事类A
 **/
public class ConcreteColleagueA extends Colleague {
    public ConcreteColleagueA(Mediator mediator) {
        super(mediator);
    }

    @Override
    public void operation(String message) {
        System.out.println("ConcreteColleagueA.operation(message) = " + message);
        mediator.send(message, this);
    }

    public void action(String message) {
        System.out.println("ConcreteColleagueA.action(message) = " + message);
    }
}
