package com.lp.demo.action.leetcode_in_action;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @create 2020/9/24 21:18
 * @auther outman
 **/
public class ListDemo {
    public static void main(String[] args) {
        // 从尾到头打印链表
        ListNode listNode = new ListNode(3);
        listNode.next = new ListNode(6);
        listNode.next.next = new ListNode(9);
        listNode.next.next.next = new ListNode(7);

        System.out.println(printListFromTailToHead(listNode));
        System.out.println(printListReverse(listNode));
    }

    /**
     * 链表节点
     */
    public static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) {
            this.val = val;
        }
    }
    /**
     * 双向链表节点
     */
    public static class DoubleListNode {
        int val;
        DoubleListNode next;
        DoubleListNode pre;
        DoubleListNode(){
        }
        DoubleListNode(int val){
            this.val = val;
            next = null;
            pre = null;
        }
    }

    /**
     * 标签：数组
     * 题目：从尾到头打印链表
     * 描述：输入一个链表的头结点，从尾到头反过来打印出每个节点的值。
     * 示例：输入[3,6,9,7] ,输出[7,9,6,3]
     * 思路：使用栈，
     * 思路2：递归，
     */
    public static List<Integer> printListFromTailToHead(ListNode listNode) {
        List<Integer> list = new ArrayList<>();
        Stack<ListNode> stack = new Stack<>();

        while (listNode != null) {
            stack.push(listNode);
            listNode = listNode.next;
        }

        while (!stack.isEmpty()) {
            list.add(stack.pop().val);
        }
        return list;
    }
    /**
     * 方法2：使用递归
     */
    public static List<Integer> printListReverse(ListNode listNode) {
        List<Integer> list = new ArrayList<>();
        if (listNode != null) {
            if (listNode.next != null) {
                list = printListReverse(listNode.next);
            }
            list.add(listNode.val);
        }
        return list;
    }
}
