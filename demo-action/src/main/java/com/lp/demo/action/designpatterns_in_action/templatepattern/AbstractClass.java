package com.lp.demo.action.designpatterns_in_action.templatepattern;

/**
 * @author lp
 * @date 2022/7/21 22:51
 * @desc 抽象类定义操作步骤
 **/
public abstract class AbstractClass {

    /**
     * 步骤1
     */
    public abstract void step1();

    /**
     * 步骤2
     */
    public abstract void step2();

    /**
     * 模板方法，具体步骤执行顺序
     */
    public void execute() {
        step1();
        step2();
    }
}
