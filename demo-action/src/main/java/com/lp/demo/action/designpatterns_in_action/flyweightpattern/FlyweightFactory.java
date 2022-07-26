package com.lp.demo.action.designpatterns_in_action.flyweightpattern;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lp
 * @date 2022/7/26 23:05
 * @desc 4.定义一个享元工厂，用来创建并管理Flyweight对象
 *         不存在时，创建一个共享对象
 *         存在时，返回共享对象
 **/
public class FlyweightFactory {
    private static Map<Integer, Flyweight> flyweights = new HashMap<>();

    public static Flyweight getFlyweight(int key) {
        Flyweight flyweight = flyweights.get(key);
        if (flyweight == null) {
            flyweight = new ConcreteFlyweight();
            flyweights.put(key, flyweight);
            System.out.println("FlyweightFactory.addFlyweight(key) = " + key);
        }
        return flyweight;
    }

    public static Flyweight getUnsharedFlyweight(int key) {
        Flyweight flyweight = flyweights.get(key);
        if (flyweight == null) {
            flyweight = new UnsharedConcreteFlyweight();
            flyweights.put(key, flyweight);
            System.out.println("FlyweightFactory.addUnsharedFlyweight(key) = " + key);
        }
        return flyweight;
    }

    public static void removeFlyweight(int key) {
        flyweights.remove(key);
        System.out.println("FlyweightFactory.removeFlyweight(key) = " + key);
    }
}
