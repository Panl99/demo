package com.lp.demo.action.designpatterns_in_action.builderpattern;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lp
 * @date 2022/7/24 21:30
 * @desc 1.产品类，由多个部件组成
 **/
public class Product {

    List<String> parts = new ArrayList<>();

    /**
     * 添加产品部件
     * @param part 部件
     */
    public void add(String part) {
        parts.add(part);
    }

    /**
     * 打印所有部件
     */
    public void show() {
        parts.forEach(System.out::println);
    }
}
