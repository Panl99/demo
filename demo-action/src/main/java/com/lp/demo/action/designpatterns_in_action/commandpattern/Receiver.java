package com.lp.demo.action.designpatterns_in_action.commandpattern;

/**
 * @author lp
 * @date 2022/7/28 21:56
 * @desc 2.定义命令的接收、执行者类
 **/
public class Receiver {
    /**
     * 接收命令并执行
     * @param command
     */
    public void action(String command) {
        System.out.println("Receiver.action(command) = " + command);
    }
}
