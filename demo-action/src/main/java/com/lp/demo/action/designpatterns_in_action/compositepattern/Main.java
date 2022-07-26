package com.lp.demo.action.designpatterns_in_action.compositepattern;

import com.alibaba.fastjson.JSON;

/**
 * @author lp
 * @date 2022/7/26 21:55
 * @desc 2.使用组合模式
 **/
public class Main {
    public static void main(String[] args) {
        TreeNode nodeA = new TreeNode("A");
        TreeNode nodeB = new TreeNode("B");
        TreeNode nodeC = new TreeNode("C");
        TreeNode nodeD = new TreeNode("D");
        nodeA.add(nodeB);
        nodeA.add(nodeC);
        nodeB.add(nodeD);
        System.out.println(JSON.toJSONString(nodeA));
        /**
         * {
         *     "name":"A",
         *     "children":[
         *         {
         *             "name":"B",
         *             "children":[
         *                 {
         *                     "name":"D",
         *                     "children":[ ]
         *                 }
         *             ]
         *         },
         *         {
         *             "name":"C",
         *             "children":[ ]
         *         }
         *     ]
         * }
         */
    }
}
