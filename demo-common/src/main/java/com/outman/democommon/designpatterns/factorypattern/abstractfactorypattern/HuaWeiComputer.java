package com.outman.democommon.designpatterns.factorypattern.abstractfactorypattern;

/**
 * @create 2021/1/27 23:02
 * @auther outman
 * @description
 **/
public class HuaWeiComputer implements Computer{
    @Override
    public String internet() {
        return "surf the internet by huawei computer";
    }
}
