package com.outman.demo.leetcode;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @create 2020/9/6 21:39
 * @auther outman
 **/
public class LeetCode {

     public class TreeNode {
         int val;
         TreeNode left;
         TreeNode right;
         TreeNode(int x) { val = x; }
     }
    /**
     * 链接：https://leetcode-cn.com/problems/binary-tree-level-order-traversal-ii
     * 题目：107. 二叉树的层次遍历 II
     * 描述：给定一个二叉树，返回其节点值自底向上的层次遍历。（即按从叶子节点所在层到根节点所在的层，逐层从左向右遍历）
     * 标签：树、广度优先遍历
     * 示例：给定二叉树 [3,9,20,null,null,15,7]。返回 [ [15,7], [9,20], [3] ]
     * 思路：可以使用队列实现，队列先进先出 符合一层一层遍历的逻辑。（栈：先进后出 适合深度优先遍历 也就是递归的逻辑）
     */
    public static List<List<Integer>> levelOrderBottom(TreeNode root) {
        //需要使用LinkedList初始化，不然找不到addFirst方法
        LinkedList<List<Integer>> result = new LinkedList<>();
        if (root == null) {
            return result;
        }

        Queue<TreeNode> nodeQueue = new LinkedList<>();
        nodeQueue.offer(root);
        while (!nodeQueue.isEmpty()) {
            List<Integer> oneLevel = new LinkedList<>();
            //每次取出一层所有数据
            int count = nodeQueue.size();
            for (int i = 0; i < count; i++) {
                //移除并返问队列头部的元素,如果队列为空，则返回null
                TreeNode node = nodeQueue.poll();
                oneLevel.add(node.val);
                if (node.left != null) {
                    //添加一个元素并返回true,如果队列已满，则返回false。(使用add会抛异常)
                    nodeQueue.offer(node.left);
                }
                if (node.right != null) {
                    nodeQueue.offer(node.right);
                }
            }
            //每次往队列头塞
            result.addFirst(oneLevel);
        }
        return result;
    }


}
