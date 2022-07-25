package com.lp.demo.action.designpatterns_in_action.statepattern;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author lp
 * @date 2022/7/25 22:07
 * @desc 2.定义Context上下文类
 *         存储状态、和执行不同状态下的行为
 **/
@Data
@AllArgsConstructor
public class Context {

    /**
     * 状态设置通过setState()方法完成。
     * 具体动作执行通过handle()方法。
     */
    private AbstractState state;

    /**
     * 对不同的状态进行处理
     */
    public void handle() {
        this.state.handle(this);
    }
}
