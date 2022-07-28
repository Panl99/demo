package com.lp.demo.action.designpatterns_in_action.commandpattern;

/**
 * @author lp
 * @date 2022/7/28 22:01
 * @desc 4.定义调用者类
 **/
public class Invoker {
    // 持有Command实例
    private Command command;

    public Invoker(Command command) {
        this.command = command;
    }

    /**
     * 调用action方法发送命令
     * @param commandMessage
     */
    public void action(String commandMessage) {
        System.out.println("Invoker.action(commandMessage) = " + commandMessage);
        this.command.execute(commandMessage);
    }
}
