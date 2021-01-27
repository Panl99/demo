package com.outman.democommon.designpatterns.factorypattern;

/**
 * @create 2021/1/25 23:11
 * @auther outman
 **/
public class Main {
    public static void main(String[] args) {
        //1
//        Factory factory = new Factory();
//        Phone hw = factory.createPhone("HuaWei");
//        Phone iphone = factory.createPhone("IPhone");
//        System.out.println(hw.brand());
//        System.out.println(iphone.brand());

        //2
        Phone hw = Factory.createPhone("huawei");
        System.out.println(hw.brand());

        //3
        Phone iphone = Factory.createPhone(PhoneEnum.IPHONE);
        Phone xm = Factory.createPhone(PhoneEnum.XIAOMI);
        System.out.println(iphone.brand());
        System.out.println(xm.brand());

    }
}
