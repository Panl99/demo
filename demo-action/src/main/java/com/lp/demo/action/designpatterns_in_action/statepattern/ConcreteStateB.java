package com.lp.demo.action.designpatterns_in_action.statepattern;

/**
 * @author lp
 * @date 2022/7/25 22:17
 * @desc 4.定义具体状态B
 *         实现一个跟Context的一个状态（B状态）相关的行为
 **/
public class ConcreteStateB extends AbstractState {
    @Override
    public void handle(Context context) {
        System.out.println("ConcreteStateB.handle()...");
        // 设置B状态的下一状态为A状态
//        context.setState(new ConcreteStateA());
    }
}
