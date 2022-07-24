package com.lp.demo.action.designpatterns_in_action.adapterpattern.interface_adapter;

/**
 * @author lp
 * @date 2022/7/24 23:32
 * @desc 1.定义公共接口
 **/
public interface CommonSource {
    /**
     * 更新名字
     */
    void updateName();

    /**
     * 更新全部
     */
    void updateAll();
}
