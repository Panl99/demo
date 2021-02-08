package com.outman.democommon.designpatterns.factorypattern.abstractfactorypattern;

/**
 * @create 2021/1/27 22:52
 * @auther outman
 * @description
 **/
public abstract class AbstractFactory {
    public abstract Phone createPhone(String brand);
    public abstract Computer createComputer(String brand);

}
