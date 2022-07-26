package com.lp.demo.action.designpatterns_in_action.flyweightpattern;

/**
 * @author lp
 * @date 2022/7/26 22:57
 * @desc 1.定义一个类是所有享元类的接口或超类
 *         可以接受并作用于外部状态
 **/
public interface Flyweight {
    /**
     * 操作方法
     * @param extrinsicState 外部状态
     */
    void operation(int extrinsicState);
}
