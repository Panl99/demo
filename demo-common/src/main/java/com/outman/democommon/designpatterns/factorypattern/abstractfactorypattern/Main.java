package com.outman.democommon.designpatterns.factorypattern.abstractfactorypattern;


/**
 * @create 2021/1/25 23:11
 * @auther outman
 **/
public class Main {
    public static void main(String[] args) {

        AbstractFactory phoneFactory = new PhoneFactory();
        Phone hwPhone = phoneFactory.createPhone("huawei");
        Phone xmPhone = phoneFactory.createPhone("xiaomi");
        System.out.println(hwPhone.call());
        System.out.println(xmPhone.call());

        AbstractFactory computerFactory = new ComputerFactory();
        Computer hwComputer = computerFactory.createComputer("huawei");
        Computer xmComputer = computerFactory.createComputer("xiaomi");
        System.out.println(hwComputer.internet());
        System.out.println(xmComputer.internet());
    }
}
