package com.lp.demo.action.designpatterns_in_action.prototypepattern;


import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author lp
 * @date 2022/7/23 22:10
 * @desc 1.浅拷贝方式实现原型模式
 **/
@Data
@AllArgsConstructor
public class ShallowCopy implements Cloneable {

    private Integer id;
    private String name;
    private Detail detail;

    /**
     * 浅拷贝：是指拷贝时只拷贝对象本身（包括对象中的基本变量），而不拷贝对象包含的引用所指向的对象，
     * 拷贝出来的对象的所有变量的值都含有与原来对象相同的值，而所有对其他对象的引用都指向原来的对象，
     * 简单地说，浅拷贝只拷贝对象不拷贝引用。
     */
    @Override
    public ShallowCopy clone() throws CloneNotSupportedException {
        return (ShallowCopy) super.clone();
    }
}
