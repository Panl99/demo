package com.lp.demo.action.designpatterns_in_action.adapterpattern;

/**
 * @author lp
 * @date 2022/7/24 23:13
 * @desc 3.定义适配器类（类适配器模式）
 **/
public class Adapter extends Source implements Target {
    @Override
    public void updateName() {
        System.out.println("Adapter.updateName()...");
    }
}
