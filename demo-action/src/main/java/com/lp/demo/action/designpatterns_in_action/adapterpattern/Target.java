package com.lp.demo.action.designpatterns_in_action.adapterpattern;

/**
 * @author lp
 * @date 2022/7/24 23:01
 * @desc 2.定义目标接口
 *         可以是具体的类、抽象类、接口
 **/
public interface Target {
    /**
     * 更新名字
     */
    void updateName();

    /**
     * 更新全部，为Source中的待适配方法
     */
    void updateAll();
}
