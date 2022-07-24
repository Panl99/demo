package com.lp.demo.action.designpatterns_in_action.observerpattern;

/**
 * @author lp
 * @date 2022/7/24 22:25
 * @desc 5.定义具体的观察者B
 **/
public class ConcreteObserverB extends Observer {

    @Override
    public void update(String message) {
        System.out.println("ConcreteObserverB.update()...message = " + message);
    }
}
