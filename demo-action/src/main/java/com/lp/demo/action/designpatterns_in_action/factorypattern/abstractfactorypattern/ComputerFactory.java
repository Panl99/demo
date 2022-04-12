package com.lp.demo.action.designpatterns_in_action.factorypattern.abstractfactorypattern;


/**
 * @create 2021/1/27 23:21
 * @auther outman
 * @description
 **/
public class ComputerFactory extends AbstractFactory{
    @Override
    public Phone createPhone(String brand) {
        return null;
    }

    @Override
    public Computer createComputer(String brand) {
        switch (brand) {
            case "huawei":
                return new HuaWeiComputer();
            case "xiaomi":
                return new XiaoMiComputer();
            default:
                System.out.println("no such computer " + brand);
                return null;
        }
    }
}
