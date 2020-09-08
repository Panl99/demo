package com.outman.demo.leetcode;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * @create 2020/9/7 22:43
 * @auther outman
 **/
public class TopkFrequentElements {
    /**
     * 链接：https://leetcode-cn.com/problems/top-k-frequent-elements
     * 题目：347. 前 K 个高频元素
     * 描述：给定一个非空的整数数组，返回其中出现频率前 k 高的元素。
     * 标签：堆、哈希表
     * 示例：输入: nums = [1,1,1,2,2,3], k = 2 输出: [1,2]
     *      输入: nums = [1], k = 1  输出: [1]
     * 思路：堆方法：遍历整个数组，使用哈希表记录每个数字出现的次数，形成一个「出现次数数组」，找出该数组前k大的值即可。
     *      「出现次数数组」找出前k大的值：建立一个小顶堆，然后遍历「出现次数数组」：
     *          1、如果堆的元素个数小于 k，就可以直接插入堆中。
     *          2、如果堆的元素个数等于 k，则检查堆顶与当前出现次数的大小。如果堆顶更大，说明至少有 k 个数字的出现次数比当前值大，故舍弃当前值；否则，就弹出堆顶，并将当前值插入堆中。
     *      遍历完成后，堆中的元素就代表了「出现次数数组」中前 k 大的值。
     * 提示：你可以假设给定的 k 总是合理的，且 1 ≤ k ≤ 数组中不相同的元素的个数。
     *      你的算法的时间复杂度必须优于 O(n log n) , n 是数组的大小。
     *      题目数据保证答案唯一，换句话说，数组中前 k 个高频元素的集合是唯一的。
     *      你可以按任意顺序返回答案。
     * 时间复杂度：O(Nlogk)
     * 空间复杂度：O(N)
     */
    public static int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> occurrences = new HashMap<Integer, Integer>();
        for (int num : nums) {
            occurrences.put(num,occurrences.getOrDefault(num, 0) + 1);
        }
        //优先队列
        PriorityQueue<int[]> queue = new PriorityQueue<int[]>(new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[1] - o2[1];
            }
        });
        for (Map.Entry<Integer, Integer> entry : occurrences.entrySet()) {
            int num = entry.getKey();
            int count = entry.getValue();
            if (queue.size() == k) {
                if (queue.peek()[1] < count) {
                    queue.poll();
                    queue.offer(new int[]{num, count});
                }
            } else {
                queue.offer(new int[]{num, count});
            }
        }
        int[] ret = new int[k];
        for (int i = 0; i < k; ++i) {
            ret[i] = queue.poll()[0];
        }
        return ret;
    }

    public static void main(String[] args) {
        int[] nums = {1,1,1,2,2,3};
        int k = 2;

        int[] topk = topKFrequent(nums, k);
        System.out.println(Arrays.toString(topk)); //[2, 1]

        /*-------------------------------------*/
        int[] nums2 = {1,2,2,2,3,3,4,4,5,5,5,5};
        int i = 2;

        int[] topk2 = topKFrequent2(nums2, i);
        System.out.println(Arrays.toString(topk2)); //[5, 2]
    }

    //使用流
    public static int[] topKFrequent2(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : nums) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }
        return map.entrySet()
                .stream()
                .sorted((m1, m2) -> m2.getValue() - m1.getValue())
                .limit(k)
                .mapToInt(Map.Entry::getKey)
                .toArray();
    }
}
