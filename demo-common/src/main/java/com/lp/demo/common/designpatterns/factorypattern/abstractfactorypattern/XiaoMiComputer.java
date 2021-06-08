package com.lp.demo.common.designpatterns.factorypattern.abstractfactorypattern;

/**
 * @create 2021/1/27 23:05
 * @auther outman
 * @description
 **/
public class XiaoMiComputer implements Computer{
    @Override
    public String internet() {
        return "surf the internet by xiaomi computer";
    }
}
