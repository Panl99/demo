package com.lp.demo.action.designpatterns_in_action.factorypattern.abstractfactorypattern;

/**
 * @create 2021/1/27 22:52
 * @auther outman
 * @description
 **/
public abstract class AbstractFactory {
    public abstract Phone createPhone(String brand);
    public abstract Computer createComputer(String brand);

}
