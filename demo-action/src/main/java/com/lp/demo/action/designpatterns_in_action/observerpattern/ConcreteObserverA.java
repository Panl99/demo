package com.lp.demo.action.designpatterns_in_action.observerpattern;

/**
 * @author lp
 * @date 2022/7/24 22:25
 * @desc 4.定义具体的观察者A
 **/
public class ConcreteObserverA extends Observer {

    @Override
    public void update(String message) {
        System.out.println("ConcreteObserverA.update()...message = " + message);
    }
}
