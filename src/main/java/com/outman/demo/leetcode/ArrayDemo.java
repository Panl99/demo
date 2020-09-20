package com.outman.demo.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * @create 2020/9/17 22:04
 * @auther outman
 **/
public class ArrayDemo {
    public static void main(String[] args) {
        int[][] matrix = {{1,2,8,9},{2,4,9,12},{4,7,10,13},{6,8,11,15}};
        int[] nums = {1,2,3};

        // 找出数组中重复数字


        // 二维数组中查找目标值
        System.out.println(findTarget(matrix, 7)); //true
        System.out.println(findTarget(matrix, 5)); //false

        // 子集
        System.out.println(subsets(nums));

    }
    /**
     * 标签：数组
     * 题目：找出数组中重复数字
     * 描述：一个长度为 n 的数组，所有数字在 0 ~ n-1范围内，数组中某些数字重复，但不知道有几个数字重复，也不知道每个数字重复了几次。
     *      找出数组中任意一个重复数字。
     * 示例：输入长度为 7 的数组 {2,3,1,0,2,5,3}，输出：2或者3
     * 思路：可以使用数组下标定位元素
     */

    /**
     * 标签：数组
     * 题目：二维数组中查找目标值
     * 描述：在一个二维数组中，每一行都按照从左到右递增的顺序排序，每一列都按照从上到下递增的顺序排序。请完成一个函数，输入这样的一个二维数组和一个整数，判断数组中是否含有该整数。
     * 示例：输入数组{{1,2,8,9},{2,4,9,12},{4,7,10,13},{6,8,11,15}} 查找数字7，返回true，查找5，返回false
     * 思路：从右上角（或左下角）开始找，查找的数字比其大，“删除”第一行，查找数字比其小，“删除”最后一列
     */
    public static boolean findTarget(int[][] matrix, int target) {
        int rows = matrix.length;
        int columns = matrix[0].length;

        if (matrix != null && rows > 0 && columns > 0) {
            int row = 0;
            int column = columns - 1;
            while (row < rows && column >= 0) {
                if (matrix[row][column] == target) {
                    return true;
                }
                if (matrix[row][column] > target) {
                    column--;
                } else {
                    row++;
                }
            }
        }
        return false;
    }

    /**
     * 链接：https://leetcode-cn.com/problems/subsets/
     * 标签：数组、位运算、回溯算法
     * 题目：子集
     * 描述：给定一组不含重复元素的整数数组 nums，返回该数组所有可能的子集（幂集）。
     * 示例：输入数组 nums = [1,2,3] 输出:[[], [1], [2], [1, 2], [3], [1, 3], [2, 3], [1, 2, 3]]
     * 思路：位运算：记原序列中元素总数为n，原序列中每个数字有两种状态-[在子集中]-[不在子集中]。用1表示在子集中，0表示不在子集中，那么每个子集都可以对应一个长度为n的0/1序列，第i位表示ai是否在子集中。
     *      例如：n = 3；a = {5,2}
     *      0/1序列   子集      0/1序列对应的二进制数
     *      00        {}       0
     *      01        {2}      1
     *      10        {5}      2
     *      11        {5,2}    3
     * 时间复杂度：O(n×2^n)。一共 2^n 个状态，每种状态需要 O(n) 的时间来构造子集。
     * 空间复杂度：O(n)。即构造子集使用的临时数组 t 的空间代价。
     */
    public static List<List<Integer>> subsets(int[] nums) {
        List<Integer> t = new ArrayList<>();
        List<List<Integer>> ans = new ArrayList<>();

        int n = nums.length;
        for (int i = 0; i < (1 << n); i++) {
            t.clear();
            for (int j = 0; j < n; j++) {
                //System.out.println(i +"---"+(1 << j)+"---"+ (i & (1 << j)));
                if ((i & (1 << j)) != 0) {
                    t.add(nums[j]);
                }
            }
            ans.add(new ArrayList<Integer>(t));
        }
        return ans;
    }

}
