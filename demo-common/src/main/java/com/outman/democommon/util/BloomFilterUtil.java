package com.outman.democommon.util;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.util.ArrayList;
import java.util.List;

/**
 * @create 2021/3/17 23:43
 * @auther lp
 * @description
 **/
public class BloomFilterUtil {
    private static int size = 1000000;
    /**
     * 不设置误判率，默认0.03
     */
    private static BloomFilter<Integer> bf = BloomFilter.create(Funnels.integerFunnel(), size, 0.01);

    public static void main(String[] args) {
        for (int i = 0; i < size; i++) {
            bf.put(i);
        }

        int targetNum = 113355;
        long startTime = System.nanoTime();
        if (bf.mightContain(targetNum)) {
            System.out.println("find it : "+ targetNum);
        }
        long endTime = System.nanoTime();
        System.out.println("耗时："+ (endTime - startTime));

        //测试误判率
        List<Integer> list = new ArrayList<>(1000);
        for (int j = size; j < size + 10000; j++) {
            if (bf.mightContain(j)) {
                list.add(j);
            }
        }
        System.out.println("误判个数："+ list.size());
        System.out.println("误判值："+ list);
    }
}
