package com.lp.demo.action.designpatterns_in_action.mementopattern;

/**
 * @author lp
 * @date 2022/7/25 22:50
 * @desc 1.定义发起人Originator类
 **/
public class Originator {

    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Originator{" +
                "state='" + state + '\'' +
                '}';
    }

    /**
     * 创建备忘录，保存当前状态
     *
     * @return
     */
    public Memento createMemento() {
        return new Memento(state);
    }

    /**
     * 从备忘录恢复状态
     *
     * @param memento
     */
    public void restoreMemento(Memento memento) {
        this.state = memento.getState();
    }
}
