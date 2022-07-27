package com.lp.demo.action.designpatterns_in_action.iteratorpattern;

/**
 * @author lp
 * @date 2022/7/27 22:09
 * @desc 1.定义迭代器接口
 *          定义迭代器应该实现的方法
 **/
public interface Iterator {
    /**
     * 上一个元素
     */
    Object previous();
    /**
     * 下一个元素
     */
    Object next();
    /**
     * 是否存在下一个元素
     */
    boolean hasNext();
}
