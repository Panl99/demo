package com.lp.demo.action.leetcode_in_action;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @create 2020/9/7 21:51
 * @auther outman
 * 二叉树：[a,b,c,d,e,f,g,null,null,h,i,null,null,null,null]
 *          a
 *      b       c
 *    d   e   f   g
 *       h i
 *   先序遍历：{a,b,d,e,h,i,c,f,g} /_
 *   中序遍历：{d,b,h,e,i,a,f,c,g} /\
 *   后序遍历：{d,h,i,e,b,f,g,c,a} _\
 **/
public class TreeDemo {
    public static void main(String[] args) {
        TreeNode tree = new TreeNode(3);
        tree.left = new TreeNode(9);
        tree.right = new TreeNode(20);
        tree.right.left = new TreeNode(15);
        tree.right.right = new TreeNode(7);

        TreeNode tree2 = new TreeNode(4);
        tree2.left = new TreeNode(2);
        tree2.right = new TreeNode(7);
        tree2.left.left = new TreeNode(1);
        tree2.left.right = new TreeNode(3);
        tree2.right.left = new TreeNode(6);
        tree2.right.right = new TreeNode(9);

        // 左叶子之和
        // 递归
        System.out.println(sumOfLeftLeaves(tree));


        // 二叉树的层次遍历 II
        // 从下向上的层次遍历
        List<List<Integer>> list = levelOrderBottom(tree);
        System.out.println(list);

        // 二叉树的层序遍历
        // 从上向下的层次遍历
        List<List<Integer>> list2 = levelOrder(tree);
        System.out.println(list2);

        // 层序遍历二叉树
        System.out.println(PrintFromTopToBottom(tree));

        // 翻转二叉树
        TreeNode resTree = invertTree(tree2);
        List invertTreeList = new ArrayList();
        invertTreeList.add(resTree.val);
        invertTreeList.add(resTree.left.val);
        invertTreeList.add(resTree.right.val);
        invertTreeList.add(resTree.left.left.val);
        invertTreeList.add(resTree.left.right.val);
        invertTreeList.add(resTree.right.left.val);
        invertTreeList.add(resTree.right.right.val);
        System.out.println(invertTreeList);

        // 后序遍历二叉树 [9, 15, 7, 20, 3]
        System.out.println(postorderTraversal(tree));
        // 前序遍历二叉树 [3, 9, 20, 15, 7]
        System.out.println(preorderTraversal(tree));
        // 中序遍历二叉树 [9, 3, 15, 20, 7]
        System.out.println(inorderTraversal(tree));
    }

    /**
     * 树节点
     */
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }

    /**
     * 链接：https://leetcode-cn.com/problems/sum-of-left-leaves/
     * 题目：404. 左叶子之和
     * 描述：计算给定二叉树的所有左叶子之和。
     * 标签：树
     * 示例：给定二叉树 [3,9,20,null,null,15,7]。在这个二叉树中，有两个左叶子，分别是 9 和 15，所以返回 24
     * 思路：递归，一个节点存在左子节点，且左子节点不存在子节点，那么左子节点为该节点的左叶子。
     * 时间复杂度：
     * 空间复杂度：
     */
    public static int sumOfLeftLeaves(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int sum = 0;
        if (root.left != null && root.left.left == null && root.left.right == null) {
            sum = root.left.val;
        }
        return sum + sumOfLeftLeaves(root.left) + sumOfLeftLeaves(root.right);
    }

    /**
     * 链接：https://leetcode-cn.com/problems/binary-tree-level-order-traversal-ii
     * 题目：107. 二叉树的层次遍历 II
     * 描述：给定一个二叉树，返回其节点值自底向上的层次遍历。（即按从叶子节点所在层到根节点所在的层，逐层从左向右遍历）
     * 标签：树、广度优先遍历
     * 示例：给定二叉树 [3,9,20,null,null,15,7]。返回 [ [15,7], [9,20], [3] ]
     * 思路：可以使用队列实现，队列先进先出 符合一层一层遍历的逻辑。（栈：先进后出 适合深度优先遍历 也就是递归的逻辑）
     * 时间复杂度：O(N)
     * 空间复杂度：O(N)
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
                //还存在子节点，加回到队列
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

    /**
     * 链接：https://leetcode-cn.com/problems/binary-tree-level-order-traversal
     * 题目：102. 二叉树的层序遍历
     * 描述：给你一个二叉树，请你返回其按 层序遍历 得到的节点值。 （即逐层地，从左到右访问所有节点）。
     * 标签：树、广度优先遍历
     * 示例：给定二叉树 [3,9,20,null,null,15,7]。返回 [ [3], [9,20], [15,7] ]
     * 思路：可以使用队列实现，队列先进先出 符合一层一层遍历的逻辑。（栈：先进后出 适合深度优先遍历 也就是递归的逻辑）
     */
    public static List<List<Integer>> levelOrder(TreeNode root) {
        //需要使用LinkedList初始化，不然找不到addFirst方法
        List<List<Integer>> result = new LinkedList<>();
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
                //还存在子节点，加回到队列
                if (node.left != null) {
                    //添加一个元素并返回true,如果队列已满，则返回false。(使用add会抛异常)
                    nodeQueue.offer(node.left);
                }
                if (node.right != null) {
                    nodeQueue.offer(node.right);
                }
            }
            //与上一题差别
            result.add(oneLevel);
        }
        return result;
    }

    /**
     * 剑指offer23:层序遍历二叉树
     * @param root
     * @return [3, 9, 20, 15, 7]
     * 解法一：迭代，间复杂度：O(n)，空间复杂度：O(n)
     */
    public static ArrayList<Integer> PrintFromTopToBottom(TreeNode root) {
        ArrayList<Integer> list = new ArrayList<>();
        if (root == null) {
            return list;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            list.add(node.val);
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
        }
        return list;
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

    /**
     * 145. 二叉树的后序遍历
     * 描述：给定一个二叉树，返回它的后序遍历。
     * 示例：                      1
     *      输入：[1,null,2,3]        2
     *      输出：[3,2,1]           3
     * 思路：后序遍历顺序：左子树——右子树——根节点
     *      递归：postorder(root) 表示当前遍历到 root 节点的答案。按照定义，我们只要递归调用 postorder(root->left) 来遍历 root 节点的左子树，然后递归调用 postorder(root->right) 来遍历 root 节点的右子树，最后将 root 节点的值加入答案即可，递归终止的条件为碰到空节点。
     *
     */
    public static List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<Integer>();
        postorder(root, res);
        return res;
    }
    public static void postorder(TreeNode root, List<Integer> res) {
        if (root == null) {
            return;
        }
        postorder(root.left, res);
        postorder(root.right, res);
        res.add(root.val);
    }
    /**
     * 144. 二叉树的前序遍历
     * 描述：给定一个二叉树，返回它的前序遍历。
     * 示例：                      1
     *      输入：[1,null,2,3]        2
     *      输出：[1,2,3]           3
     * 思路：前序遍历顺序：根节点——左子树——右子树
     *      递归：
     *
     */
    public static List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<Integer>();
        preorder(root, res);
        return res;
    }
    public static void preorder(TreeNode root, List<Integer> res) {
        if (root == null) {
            return;
        }
        res.add(root.val);
        preorder(root.left, res);
        preorder(root.right, res);
    }
    /**
     * 94. 二叉树的中序遍历
     * 描述：给定一个二叉树，返回它的中序遍历。
     * 示例：                      1
     *      输入：[1,null,2,3]        2
     *      输出：[1,2,3]           3
     * 思路：中序遍历顺序：左子树——根节点——右子树
     *      递归：
     */
    public static List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<Integer>();
        inorder(root, res);
        return res;
    }
    public static void inorder(TreeNode root, List<Integer> res) {
        if (root == null) {
            return;
        }
        inorder(root.left, res);
        res.add(root.val);
        inorder(root.right, res);
    }

}
