package com.lp.demo.action.designpatterns_in_action.adapterpattern.interface_adapter;

/**
 * @author lp
 * @date 2022/7/24 23:34
 * @desc 2.定义抽象适配器类，重写CommonSource但不实现
 **/
public abstract class AbstractAdapter implements CommonSource {

    @Override
    public void updateName() {

    }

    @Override
    public void updateAll() {

    }
}
