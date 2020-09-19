package com.outman.demo.leetcode;

/**
 * @create 2020/9/17 22:04
 * @auther outman
 **/
public class ArrayDemo {
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

    public static void main(String[] args) {
        //二维数组中查找目标值
        int[][] matrix = {{1,2,8,9},{2,4,9,12},{4,7,10,13},{6,8,11,15}};
        System.out.println(findTarget(matrix, 7)); //true
        System.out.println(findTarget(matrix, 5)); //false

    }
}
