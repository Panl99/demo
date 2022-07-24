package com.lp.demo.action.designpatterns_in_action.builderpattern;

/**
 * @author lp
 * @date 2022/7/24 21:36
 * @desc 2.定义抽象建造者类
 *         确定产品由哪几个部件组成，并声明一个建造产品后的结果
 **/
public abstract class Builder {
    /**
     * 构建部件A
     */
    public abstract void buildPartA();

    /**
     * 构建部件B
     */
    public abstract void buildPartB();

    /**
     * 获取构建后的结果
     * @return
     */
    public abstract Product getResult();
}
