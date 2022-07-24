package com.lp.demo.action.designpatterns_in_action.prototypepattern;


import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author lp
 * @date 2022/7/23 22:10
 * @desc 2.深拷贝方式实现原型模式
 **/
@Data
@AllArgsConstructor
public class DeepCopy implements Cloneable {

    private Integer id;
    private String name;
    private Detail detail;//引用对象

    /**
     * 深拷贝：不仅拷贝对象本身，而且还拷贝对象包含的引用所指向的对象，
     * 拷贝出来的对象的所有变量（不包含那些引用其他对象的变量）的值都含有与原来对象的相同的值，那些引用其他对象的变量将指向新复制出来的新对象，而不指向原来的对象，
     * 简单地说，深拷贝不仅拷贝对象，而且还拷贝对象包含的引用所指向的对象。
     */
    @Override
    public DeepCopy clone() throws CloneNotSupportedException {
        DeepCopy deepCopy = (DeepCopy) super.clone();
        deepCopy.detail = this.detail.clone();
        return deepCopy;
    }

}
