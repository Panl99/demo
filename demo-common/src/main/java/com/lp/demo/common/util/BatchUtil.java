package com.lp.demo.common.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BatchUtil {

    private static final int NUM = 3;

    /**
     * 批量操作
     *
     * @param c
     * @param list
     * @param <T>
     */
    public static <T> void invoke(Consumer<List<T>> c, List<T> list) {
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        int index = 0;
        while (list.size() > index + NUM){
            List<T> subList = list.subList(index, index + NUM);
            c.accept(subList);
            index += NUM;
        }
        List<T> subList = list.subList(index, list.size());
        if (subList.size() > 0) {
            c.accept(subList);
        }
    }


    public static void main(String[] args) {
        List<Tuple> tuples = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            tuples.add(new Tuple(i, i < 5 ? Boolean.TRUE : Boolean.FALSE));
        }
        invoke(System.out::println, tuples);
        /**
         * [[0, true], [1, true], [2, true]]
         * [[3, true], [4, true], [5, false]]
         * [[6, false], [7, false], [8, false]]
         * [[9, false]]
         */
    }
}