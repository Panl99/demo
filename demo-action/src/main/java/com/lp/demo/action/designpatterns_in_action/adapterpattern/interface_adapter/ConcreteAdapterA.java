package com.lp.demo.action.designpatterns_in_action.adapterpattern.interface_adapter;

/**
 * @author lp
 * @date 2022/7/24 23:37
 * @desc 3.定义具体适配器类A，按需实现方法
 **/
public class ConcreteAdapterA extends AbstractAdapter {

    @Override
    public void updateName() {
        System.out.println("ConcreteAdapterA.updateName()...");
    }

    @Override
    public void updateAll() {
        super.updateAll();
    }
}
