package com.lp.demo.action.designpatterns_in_action.statepattern;

/**
 * @author lp
 * @date 2022/7/25 21:24
 * @desc 5.使用状态模式
 **/
public class Main {
    public static void main(String[] args) {
        /**
         * 当前为A状态
         */
        Context context = new Context(new ConcreteStateA());
        context.handle();

        /**
         * 切换当前为B状态
         */
        context = new Context(new ConcreteStateB());
        context.handle();
    }
}
