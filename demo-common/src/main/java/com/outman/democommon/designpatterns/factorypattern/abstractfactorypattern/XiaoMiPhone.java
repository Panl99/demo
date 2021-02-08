package com.outman.democommon.designpatterns.factorypattern.abstractfactorypattern;

/**
 * @create 2021/1/27 21:34
 * @auther outman
 **/
public class XiaoMiPhone implements Phone {
    @Override
    public String call() {
        return "call by xiaomi phone";
    }
}
