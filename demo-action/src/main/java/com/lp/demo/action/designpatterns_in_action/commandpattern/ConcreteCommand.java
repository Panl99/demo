package com.lp.demo.action.designpatterns_in_action.commandpattern;

/**
 * @author lp
 * @date 2022/7/28 21:55
 * @desc 3.定义具体命令实现类
 **/
public class ConcreteCommand implements Command {
    // 持有命令接收 和执行者的实例
    private Receiver receiver;

    public ConcreteCommand(Receiver receiver) {
        this.receiver = receiver;
    }

    /**
     * 接收到命令后，交给Receiver执行
     * @param command
     */
    @Override
    public void execute(String command) {
        receiver.action(command);
    }
}
