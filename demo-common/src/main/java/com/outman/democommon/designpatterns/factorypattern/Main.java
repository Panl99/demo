package com.outman.democommon.designpatterns.factorypattern;

/**
 * @create 2021/1/25 23:11
 * @auther outman
 **/
public class Main {
    public static void main(String[] args) {
        Factory factory = new Factory();
        Phone hw = factory.createPhone("HuaWei");
        Phone iphone = factory.createPhone("IPhone");
        System.out.println(hw.brand());
        System.out.println(iphone.brand());
    }
}
