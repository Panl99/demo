package com.lp.demo.action.designpatterns_in_action.mementopattern;

/**
 * @author lp
 * @date 2022/7/25 23:00
 * @desc 3.创建状态管理者类
 *         管理备忘录：设置、获取备忘录
 **/
public class Caretaker {

    private Memento memento;

    public Caretaker(Memento memento) {
        this.memento = memento;
    }

    public Memento getMemento() {
        return memento;
    }

    public void setMemento(Memento memento) {
        this.memento = memento;
    }
}
