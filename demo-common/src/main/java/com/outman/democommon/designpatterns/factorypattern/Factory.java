package com.outman.democommon.designpatterns.factorypattern;

/**
 * @create 2021/1/25 22:52
 * @auther outman
 **/
public class Factory {
    public Phone createPhone(String phoneName) {
        if ("HuaWei".equals(phoneName)) {
            return new MyHuaWei();
        } else if ("IPhone".equals(phoneName)) {
            return new MyIPhone();
        } else {
            return null;
        }
    }
}
