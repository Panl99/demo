package com.lp.demo.action.designpatterns_in_action.flyweightpattern;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lp
 * @date 2022/7/26 22:55
 * @desc 5.使用享元模式
 **/
public class Main {
    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    public static void main(String[] args) {
        int i = COUNTER.incrementAndGet();
        // 首次获取，将创建
        Flyweight flyweight = FlyweightFactory.getFlyweight(i);
        flyweight.operation(i);
        // 移除共享对象
        FlyweightFactory.removeFlyweight(i);

        Flyweight unsharedFlyweight = FlyweightFactory.getUnsharedFlyweight(i);
        unsharedFlyweight.operation(i);

        flyweight.operation(i);
    }
}
