package com.outman.demo.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * @create 2020/9/16 21:33
 * @auther outman
 **/
public class InvertBinaryTree {
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }
    /**
     * 链接：https://leetcode-cn.com/problems/invert-binary-tree/
     * 题目：226. 翻转二叉树
     * 描述：翻转一棵二叉树。
     * 标签：树
     * 示例：给定二叉树 [4,2,7,1,3,6,9]。返回 [4,7,2,9,6,3,1]
     * 思路：递归：从根节点开始，递归地对树进行遍历，并从叶子结点先开始翻转。如果当前遍历到的节点 root 的左右两棵子树都已经翻转，那么我们只需要交换两棵子树的位置，即可完成以 root 为根节点的整棵子树的翻转。
     * 时间复杂度：O(N)
     * 空间复杂度：O(logN)。而在最坏情况下，树形成链状，空间复杂度为 O(N)
     */
    public static TreeNode invertTree(TreeNode root) {
        if (root == null) {
            return null;
        }
        TreeNode left = invertTree(root.left);
        TreeNode right = invertTree(root.right);
        root.left = right;
        root.right = left;
        return root;
    }

    public static void main(String[] args) {
        TreeNode tree = new TreeNode(4);
        tree.left = new TreeNode(2);
        tree.right = new TreeNode(7);
        tree.left.left = new TreeNode(1);
        tree.left.right = new TreeNode(3);
        tree.right.left = new TreeNode(6);
        tree.right.right = new TreeNode(9);

        TreeNode resTree = invertTree(tree);
        List list = new ArrayList();
        list.add(resTree.val);
        list.add(resTree.left.val);
        list.add(resTree.right.val);
        list.add(resTree.left.left.val);
        list.add(resTree.left.right.val);
        list.add(resTree.right.left.val);
        list.add(resTree.right.right.val);
        System.out.println(list);
    }
}
