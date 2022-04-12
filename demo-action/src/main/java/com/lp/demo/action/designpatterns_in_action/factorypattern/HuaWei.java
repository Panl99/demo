package com.lp.demo.action.designpatterns_in_action.factorypattern;

/**
 * @create 2021/1/25 23:04
 * @auther outman
 **/
public class HuaWei implements Phone {
    @Override
    public String brand() {
        return "this is a huawei phone";
    }
}
