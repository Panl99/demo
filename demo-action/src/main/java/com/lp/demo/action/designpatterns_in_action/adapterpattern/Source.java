package com.lp.demo.action.designpatterns_in_action.adapterpattern;

/**
 * @author lp
 * @date 2022/7/24 23:06
 * @desc 1.定义待适配的原有类
 **/
public class Source {
    /**
     * 待适配方法
     */
    public void updateAll() {
        System.out.println("Source.updateAll()...");
    }
}
