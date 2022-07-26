package com.lp.demo.action.designpatterns_in_action.flyweightpattern;

/**
 * @author lp
 * @date 2022/7/26 23:01
 * @desc 2.继承/实现Flyweight超类/接口，为内部状态增加存储空间
 **/
public class ConcreteFlyweight implements Flyweight {
    @Override
    public void operation(int extrinsicState) {
        System.out.println("ConcreteFlyweight.operation(extrinsicState) = " + extrinsicState);
    }
}
