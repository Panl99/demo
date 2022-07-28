package com.lp.demo.action.designpatterns_in_action.commandpattern;

/**
 * @author lp
 * @date 2022/7/28 21:52
 * @desc 5.使用命令模式
 **/
public class Main {
    public static void main(String[] args) {
        // 1.定义命令的接收和执行者
        Receiver receiver = new Receiver();
        // 2.定义命令实现类
        Command command = new ConcreteCommand(receiver);
        // 3.定义命令的调用者
        Invoker invoker = new Invoker(command);
        // 4.命令调用者发送命令
        invoker.action("let's go!");
    }
}
