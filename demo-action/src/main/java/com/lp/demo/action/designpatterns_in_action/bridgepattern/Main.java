package com.lp.demo.action.designpatterns_in_action.bridgepattern;

/**
 * @author lp
 * @date 2022/7/26 22:29
 * @desc 6.使用桥接模式
 **/
public class Main {
    public static void main(String[] args) {
        ImplementorBridge bridge = new ConcreteImplementorBridgeA();
        // 设置具体实现者A
        bridge.setImplementor(new ConcreteImplementorA());
        bridge.execute();

        // 切换到具体实现者B
        bridge.setImplementor(new ConcreteImplementorB());
        bridge.execute();
    }
}
