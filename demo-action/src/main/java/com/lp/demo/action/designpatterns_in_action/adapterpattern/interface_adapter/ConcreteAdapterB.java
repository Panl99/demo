package com.lp.demo.action.designpatterns_in_action.adapterpattern.interface_adapter;

/**
 * @author lp
 * @date 2022/7/24 23:37
 * @desc 4.定义具体适配器类B，按需实现方法
 **/
public class ConcreteAdapterB extends AbstractAdapter {

    @Override
    public void updateName() {
        super.updateName();
    }

    @Override
    public void updateAll() {
        System.out.println("ConcreteAdapterB.updateAll()...");
    }
}
