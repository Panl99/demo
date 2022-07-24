package com.lp.demo.action.designpatterns_in_action.observerpattern;

/**
 * @author lp
 * @date 2022/7/24 22:11
 * @desc 1.定义抽象观察者
 **/
public abstract class Observer {
    /**
     * 更新方法
     */
    public abstract void update(String message);
}
