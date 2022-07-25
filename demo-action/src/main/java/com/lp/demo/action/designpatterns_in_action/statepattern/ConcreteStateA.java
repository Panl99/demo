package com.lp.demo.action.designpatterns_in_action.statepattern;

/**
 * @author lp
 * @date 2022/7/25 22:17
 * @desc 3.定义具体状态A
 *         实现一个跟Context的一个状态（A状态）相关的行为
 **/
public class ConcreteStateA extends AbstractState {
    @Override
    public void handle(Context context) {
        System.out.println("ConcreteStateA.handle()...");
        // 设置A状态的下一状态为B状态
//        context.setState(new ConcreteStateB());
    }
}
