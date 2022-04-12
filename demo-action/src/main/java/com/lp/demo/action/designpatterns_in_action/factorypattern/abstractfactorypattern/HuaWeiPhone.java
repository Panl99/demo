package com.lp.demo.action.designpatterns_in_action.factorypattern.abstractfactorypattern;

/**
 * @create 2021/1/25 23:04
 * @auther outman
 **/
public class HuaWeiPhone implements Phone {
    @Override
    public String call() {
        return "call by huawei phone";
    }
}
