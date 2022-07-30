package com.lp.demo.action.designpatterns_in_action.interpreterpattern;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lp
 * @date 2022/7/30 22:10
 * @desc 4.定义全局类Context
 *       存储表达式解析出来的值，提供查询、解析后表达式的结果
 **/
public class Context {
    private Map<Expression, String> map = new HashMap<>();

    public void set(Expression key, String value) {
        map.put(key, value);
    }

    public String get(Expression key) {
        return map.get(key);
    }
}
