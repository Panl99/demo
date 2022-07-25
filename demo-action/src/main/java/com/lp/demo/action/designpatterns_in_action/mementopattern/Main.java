package com.lp.demo.action.designpatterns_in_action.mementopattern;

/**
 * @author lp
 * @date 2022/7/25 22:49
 * @desc 4.使用备忘录模式
 **/
public class Main {
    public static void main(String[] args) {
        // 设置初始状态为ON
        Originator originator = new Originator();
        originator.setState("ON");
        System.out.println("1.originator.setState = " + originator);

        // 管理者创建一个备忘录，保存初始状态
        Caretaker caretaker = new Caretaker(originator.createMemento());

        // 修改状态为OFF
        originator.setState("OFF");
        System.out.println("2.originator.updateState = " + originator);

        // 恢复原始状态
        originator.restoreMemento(caretaker.getMemento());
        System.out.println("3.originator.restoreState = " + originator);

    }
}
