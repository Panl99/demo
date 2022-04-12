package com.lp.demo.action.designpatterns_in_action.factorypattern.abstractfactorypattern;


/**
 * @create 2021/1/25 22:52
 * @auther outman
 **/
public class PhoneFactory extends AbstractFactory{

    @Override
    public Phone createPhone(String brand) {
        switch (brand) {
            case "huawei":
                return new HuaWeiPhone();
            case "xiaomi":
                return new XiaoMiPhone();
            default:
                System.out.println("no such phone " + brand);
                return null;
        }

    }

    @Override
    public Computer createComputer(String brand) {
        return null;
    }

}
