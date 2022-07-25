package com.lp.demo.action.designpatterns_in_action.mementopattern;

/**
 * @author lp
 * @date 2022/7/25 22:52
 * @desc 2.定义备忘录
 *         管理状态数据
 **/
public class Memento {

    private String state;

    public Memento(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
