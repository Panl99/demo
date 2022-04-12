package com.lp.demo.action.leetcode_in_action;

import java.util.Stack;

/**
 * @create 2020/12/6 21:03
 * @auther outman
 **/
public class StackQueueDemo {
    Stack<Integer> stack1 = new Stack<Integer>();
    Stack<Integer> stack2 = new Stack<Integer>();

    public static void main(String[] args) {

    }

    /**
     * 用两个栈实现一个队列
     * 描述：用两个栈实现一个队列。完成队列的Push和Pop操作。 队列中的元素为int类型
     */
    // 入栈
    public void push(int node) {
        stack1.push(node);
    }
    // 出栈
    public int pop() {
        if (stack2.isEmpty()) {
            while(!stack1.isEmpty()){
                stack2.push(stack1.pop());
            }
        }
        return stack2.pop();
    }

}
