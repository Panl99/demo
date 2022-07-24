package com.lp.demo.action.designpatterns_in_action.observerpattern;

/**
 * @author lp
 * @date 2022/7/24 22:22
 * @desc 3.定义具体主题
 **/
public class ConcreteSubject extends Subject {
    @Override
    public void notify(String message) {
        System.out.println("ConcreteSubject.notify()...message = " + message);
        observers.forEach(observer -> {
            observer.update(message);
        });
    }
}
