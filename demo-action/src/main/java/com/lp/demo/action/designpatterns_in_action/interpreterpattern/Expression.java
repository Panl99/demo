package com.lp.demo.action.designpatterns_in_action.interpreterpattern;

/**
 * @author lp
 * @date 2022/7/30 22:07
 * @desc 1.定义解释器接口
 **/
public interface Expression {

    /**
     * 解释方法
     * @param context
     */
    void interpret(Context context);
}
