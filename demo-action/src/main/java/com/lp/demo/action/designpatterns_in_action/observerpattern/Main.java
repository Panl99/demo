package com.lp.demo.action.designpatterns_in_action.observerpattern;


/**
 * @author lp
 * @date 2021/3/3 22:47
 * @desc 6.使用观察者模式
 **/
public class Main {
    public static void main(String[] args) {
        Subject subject = new ConcreteSubject();
        Observer observerA = new ConcreteObserverA();
        Observer observerB = new ConcreteObserverB();
        subject.add(observerA);
        subject.add(observerB);
        subject.notify("change state...");
    }
}
