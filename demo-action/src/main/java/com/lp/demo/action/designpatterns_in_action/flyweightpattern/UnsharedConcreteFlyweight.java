package com.lp.demo.action.designpatterns_in_action.flyweightpattern;

/**
 * @author lp
 * @date 2022/7/26 23:01
 * @desc 3.定义不需要共享的Flyweight子类
 **/
public class UnsharedConcreteFlyweight implements Flyweight {
    @Override
    public void operation(int extrinsicState) {
        System.out.println("UnsharedConcreteFlyweight.operation(extrinsicState) = " + extrinsicState);
    }
}
