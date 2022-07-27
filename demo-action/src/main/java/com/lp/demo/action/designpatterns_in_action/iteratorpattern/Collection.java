package com.lp.demo.action.designpatterns_in_action.iteratorpattern;

/**
 * @author lp
 * @date 2022/7/27 22:17
 * @desc 2.定义结合接口
 **/
public interface Collection {
    /**
     * 对集合元素的迭代
     * @return
     */
    Iterator iterator();

    /**
     * 获取集合元素
     * @param i
     * @return
     */
    Object get(int i);

    /**
     * 添加集合元素
     * @param o
     * @return
     */
    boolean add(Object o);

    /**
     * 集合大小
     *
     * @return
     */
    int size();
}
