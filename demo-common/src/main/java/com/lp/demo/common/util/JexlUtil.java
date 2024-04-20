package com.lp.demo.common.util;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;

import java.util.HashMap;
import java.util.Map;


@Slf4j
public class JexlUtil {
    /**
     * 计算表达式结果
     *
     * @param expression jxel表达式
     * @param dataMap    数据集
     * @return 表达式匹配结果
     */
    public static Object check(String expression, Map<String, Object> dataMap) {
        if (StringUtil.isEmpty(expression)) {
            return false;
        } else if (CollectionUtil.isEmpty(dataMap)) {
            return false;
        }
        JexlEngine jexl = new JexlBuilder().create();
        JexlExpression e = jexl.createExpression(expression);
        JexlContext jc = new MapContext();
        dataMap.forEach(jc::set);
        Object evaluate = e.evaluate(jc);
        log.info("{} => {} => {}", dataMap, expression, evaluate);
        return evaluate;
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("high", 25);
        map.put("length", 10);
        map.put("name", "everything");
        map.put("val", "CC");
        String expression = "high>=20&&high<30&&length!=20&&name==\"everything\"&&val.equals(\"CC\")";
        Object code = check(expression, map);
        System.err.println(code);
    }
}
