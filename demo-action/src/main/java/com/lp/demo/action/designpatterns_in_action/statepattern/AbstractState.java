package com.lp.demo.action.designpatterns_in_action.statepattern;

/**
 * @author lp
 * @date 2022/7/25 22:03
 * @desc 1.定义抽象状态类
 **/
public abstract class AbstractState {
    /**
     * 对不同状态进行不同的处理
     * @param context
     */
    public abstract void handle(Context context);
}
