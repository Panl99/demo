package com.lp.demo.action.designpatterns_in_action.commandpattern;

/**
 * @author lp
 * @date 2022/7/28 21:54
 * @desc 1.定义命令接口，执行方法
 **/
public interface Command {
    /**
     * 执行方法
     * @param command
     */
    void execute(String command);
}
