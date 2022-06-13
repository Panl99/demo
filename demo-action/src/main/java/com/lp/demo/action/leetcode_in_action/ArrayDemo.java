package com.lp.demo.action.leetcode_in_action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * @create 2020/9/17 22:04
 * @auther outman
 **/
public class ArrayDemo {
    public static void main(String[] args) {
        int[][] matrix = {{1,2,8,9},{2,4,9,12},{4,7,10,13},{6,8,11,15}};
        int[] nums = {1,2,3};

        // 找出数组中重复数字
        System.out.println(getNum());

        //打印不重复元素
        System.out.println(getNum2()); // 使用亦或


        // 二维数组中查找目标值
        System.out.println(findTarget(matrix, 7)); //true
        System.out.println(findTarget(matrix, 5)); //false

        // 子集
        System.out.println(subsets(nums));

        //最少步数
        System.out.println(minStept());

        //最大数
        largestNumber();
        //方法2
        largestNumber(new int[]{3,30,34,5,9});

    }
    /**
     * 标签：数组
     * 题目：找出数组中重复数字
     * 描述：一个长度为 n 的数组，所有数字在 0 ~ n-1范围内，数组中某些数字重复，但不知道有几个数字重复，也不知道每个数字重复了几次。
     *      找出数组中任意一个重复数字。
     * 示例：输入长度为 7 的数组 {2,3,1,0,2,5,3}，输出：2或者3
     * 思路：可以使用数组下标定位元素
     *      使用哈希表的话，时间复杂度：O(n)；空间复杂度：O(n)。
     */
    public static int[] getNum() {
        int[] nums = {2,2,1,1,5,4,4,7};
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            if (! res.contains(nums[i])) {
                res.add(nums[i]);
            } else {
                res.remove(Integer.valueOf(nums[i]));
                continue;
            }

        }
        for (int i : res) {
            System.out.print(i + " ");
        }
        return res.stream().mapToInt(Integer::valueOf).toArray();
    }

    /**
     * 任给一个数组，其中只有一个元素是单独出现，其他是成对出现，输出单独的元素。
     *     例如： 输入： {2,2,1,1,4,4,7}
     *     输出：7
     * 思路：使用亦或^运算：相同数字亦或结果为0,0亦或任何数都得任何数,亦或具有交换性：a^b^c = c^b^a
     *      1 0 0 1 0 1
     *    ^
     *      0 0 1 0 0 1
     * 结果：
     *      1 0 1 1 0 0
     */
    public static int getNum2() {
        int[] nums = {2,1,2,4,1,5,4};
        int result = 0;
        for (int i = 0; i < nums.length; i++) {
            result ^= nums[i];
        }
        return result;
    }

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

    /**
     * 描述：给定一个正整数数组，最大为100个成员，从第一个成员开始，走到数组最后一个成员最少的步骤数。
     *      第一步必须从第一元素开始，1<=步长<len/2, 第二步开始以所在成员的数字走相应的步数，如果目标不可达返回-1，只输出最少的步骤数
     * 输入：7 5 9 4 2 6 8 3 5 4 3 9
     * 输出：2
     */
    public static int minStept() {
        int[] nums = {7 ,5 ,9 ,4 ,2 ,6 ,8 ,3 ,5 ,4 ,3 ,9};
//        int[] nums = {3, 3, 8, 2, 4, 1, 6};
        int len = nums.length;
        int res = len/2;
        for (int i = 1; i <= len/2; i++) {
            int step = 1 + i + nums[i];
            int count = getCount(nums, step, len, 2);
            if (-1 == count) {
                continue;
            } else if (count == 2){
                return count;
            }

            if (res > count) {
                res = count;
            }
        }
        return res;
    }
    public static int getCount(int[] nums, int step, int len, int count) {
        if (step < len){
            count++;
            getCount(nums,step + nums[step - 1], len, count);
        } else if (step == len) {
            return count;
        }
        return -1;
    }

    /**
     * 179. 最大数
     * 给定一组非负整数 nums，重新排列它们每个数字的顺序（每个数字不可拆分）使之组成一个最大的整数。
     * 注意：输出结果可能非常大，所以你需要返回一个字符串而不是整数。
     * 示例 1：
     *      输入：nums = [10,2]
     *      输出："210"
     * 示例 2：
     *      输入：nums = [3,30,34,5,9]
     *      输出："9534330"
     */
    public static void largestNumber() {
        Scanner in = new Scanner(System.in);
        String strs = in.nextLine();
        String[] nums = strs.split(" ");
        int len = nums.length;
        int tmp;
        while (len != 0) {
            for (int j = 1; j < len; j++) {
                if (exchange(Integer.parseInt(nums[j - 1]), Integer.parseInt(nums[j]))) {
                    tmp = Integer.parseInt(nums[j - 1]);
                    nums[j - 1] = nums[j];
                    nums[j] = String.valueOf(tmp);
                }
            }
            len--;
        }
        StringBuilder sb = new StringBuilder();
        for (String a : nums) {
            sb.append(a);
        }
        System.out.println(sb);

    }
    public static boolean exchange(int a, int b) {
        StringBuilder sb1 = new StringBuilder();
        sb1.append(a).append(b);
        StringBuilder sb2 = new StringBuilder();
        sb2.append(b).append(a);
        int m1 = Integer.parseInt(sb1.toString());
        int m2 = Integer.parseInt(sb2.toString());
        if (m1 < m2){
            return true;
        } else {
            return false;
        }

    }
    //方法2 leetcode官解
    public static String largestNumber(int[] nums) {
        String[] strs = new String[nums.length];
        for (int i = 0; i < nums.length; i++) {
            strs[i] = String.valueOf(nums[i]);
        }
        Arrays.sort(strs, new LargerNumberComparator());
        if (strs[0].equals("0")) {
            return "0";
        }
        String str = new String();
        for (String numAsStr : strs) {
            str += numAsStr;
        }
        return str;
    }
    private static class LargerNumberComparator implements Comparator<String> {
        @Override
        public int compare(String a, String b) {
            String order1 = a + b;
            String order2 = b + a;
            return order2.compareTo(order1);
        }
    }


}
