package com.lp.demo.action.designpatterns_in_action.visitorpattern;

/**
 * @author lp
 * @date 2022/7/30 23:09
 * @desc 2.定义抽象元素接口
 **/
public interface Element {
    /**
     * 接收访问者对象
     * @param visitor
     */
    void accept(Visitor visitor);
}
