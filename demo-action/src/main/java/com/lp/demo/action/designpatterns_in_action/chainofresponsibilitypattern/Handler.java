package com.lp.demo.action.designpatterns_in_action.chainofresponsibilitypattern;

/**
 * @author lp
 * @date 2022/7/27 21:43
 * @desc 1.定义Handler接口
 *         用于规定责任链上各个环节的操作
 **/
public interface Handler {
    /**
     * 操作者方法，用于责任链上各个环节处理任务时调用
     */
    void operator();
}
