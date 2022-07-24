package com.lp.demo.action.designpatterns_in_action.builderpattern;

/**
 * @author lp
 * @date 2022/7/24 21:45
 * @desc 5.指挥者类，指挥建造过程
 **/
public class Director {
    public void construct(Builder builder) {
        builder.buildPartA();
        builder.buildPartB();
    }
}
