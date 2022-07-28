package com.lp.demo.action.designpatterns_in_action.mediatorpattern;

/**
 * @author lp
 * @date 2022/7/28 22:31
 * @desc 4.定义具体同事类B
 **/
public class ConcreteColleagueB extends Colleague {
    public ConcreteColleagueB(Mediator mediator) {
        super(mediator);
    }

    @Override
    public void operation(String message) {
        System.out.println("ConcreteColleagueB.operation(message) = " + message);
        mediator.send(message, this);
    }

    public void action(String message) {
        System.out.println("ConcreteColleagueB.action(message) = " + message);
    }
}
