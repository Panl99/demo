package com.lp.demo.action.designpatterns_in_action.iteratorpattern;

/**
 * @author lp
 * @date 2022/7/27 22:08
 * @desc 5.使用迭代器模式
 **/
public class Main {
    public static void main(String[] args) {
        // 定义集合
        Collection collection = new ConcreteCollection();
        // 向集合添加数据
        collection.add("object1...");
        collection.add("object3...");
        collection.add("object2...");
        // 使用迭代器遍历集合
        Iterator it = collection.iterator();
        while (it.hasNext()) {
            System.out.println("it.next() = " + it.next());
        }
    }

}
