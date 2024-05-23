package com.lp.demo.common.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Tuple;
import com.lp.demo.common.exception.DisplayableException;
import com.lp.demo.common.result.BaseEnum;
import com.lp.demo.common.result.ResultEnum;

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
        invoke(c, list, NUM);
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

        invoke(System.out::println, null, ResultEnum.FAIL);
    }

//    public static <T> void invoke(Consumer<List<T>> c, List<T> list) {
//        invoke(c, list, NUM);
//    }

    public static <T> void invoke(Consumer<List<T>> c, List<T> list, BaseEnum baseEnum) {
        invoke(c, list, NUM, baseEnum);
    }

    public static <T> void invoke(Consumer<List<T>> c, List<T> list, Integer code, String message) {
        invoke(c, list, NUM, code, message);
    }

    public static <T> void invoke(Consumer<List<T>> c, List<T> list, int count) {
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        accept(c, list, count);
    }

    public static <T> void invoke(Consumer<List<T>> c, List<T> list, int count, BaseEnum baseEnum) {
        if (CollectionUtil.isEmpty(list)) {
            throw new DisplayableException(baseEnum);
        }
        accept(c, list, count);
    }

    public static <T> void invoke(Consumer<List<T>> c, List<T> list, int count, Integer code, String message) {
        if (CollectionUtil.isEmpty(list)) {
            throw new DisplayableException(code, message);
        }
        accept(c, list, count);
    }

    private static <T> void accept(Consumer<List<T>> c, List<T> list, int count) {
        int index = 0;
        while (list.size() > index + count){
            List<T> subList = list.subList(index, index + count);
            c.accept(subList);
            index += count;
        }
        List<T> subList = list.subList(index, list.size());
        if (subList.size() > 0) {
            c.accept(subList);
        }
    }
}