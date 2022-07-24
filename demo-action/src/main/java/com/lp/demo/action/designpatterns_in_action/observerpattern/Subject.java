package com.lp.demo.action.designpatterns_in_action.observerpattern;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lp
 * @date 2022/7/24 22:09
 * @desc 2.定义抽象主题
 **/
public abstract class Subject {

    List<Observer> observers = new ArrayList<>();

    /**
     * 添加观察者
     *
     * @param observer 观察者对象
     */
    public void add(Observer observer) {
        observers.add(observer);
    }

    /**
     * 移除观察者
     *
     * @param observer 观察者对象
     */
    public void remove(Observer observer) {
        observers.remove(observer);
    }

    /**
     * 通知观察者
     *
     * @param message 通知消息
     */
    public abstract void notify(String message);
}
