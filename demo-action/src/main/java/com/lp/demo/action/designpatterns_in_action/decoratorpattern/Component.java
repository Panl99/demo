package com.lp.demo.action.designpatterns_in_action.decoratorpattern;

/**
 * @author lp
 * @date 2022/7/23 15:28
 * @desc 1.定义抽象对象类，可以为具体对象动态的添加职责
 **/
public abstract class Component {

    /**
     * 操作方法
     */
    public abstract void operation();
}
