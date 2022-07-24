package com.lp.demo.action.designpatterns_in_action.prototypepattern;

import java.util.Collections;

/**
 * @author lp
 * @date 2022/7/23 23:04
 * @desc 4.使用原型模式
 **/
public class Main {
    public static void main(String[] args) throws CloneNotSupportedException {
        /**
         * 浅拷贝
         */
        shallowCopyTest();

        /**
         * 深拷贝
         */
        deepCopyTest();

    }

    /**
     * 浅拷贝
     */
    public static void shallowCopyTest() throws CloneNotSupportedException {
        Detail detail = new Detail("123456", Collections.singletonList("听音乐"), "shenzhen");

        ShallowCopy shallowCopy = new ShallowCopy(1001, "浅拷贝", detail);
        System.out.println("before shallowCopy = " + shallowCopy);

        ShallowCopy shallowCopyClone = shallowCopy.clone();
        shallowCopyClone.setId(1002);
        shallowCopyClone.setName("浅拷贝克隆");
        // 这里更新克隆的shallowCopyClone引用对象的值，原型对象shallowCopy对应的引用对象的值也会被更改，
        // 浅克隆里它们指向的引用对象是同一个对象。
        shallowCopyClone.getDetail().setPhone("123457");
        shallowCopyClone.getDetail().setHobby(Collections.singletonList("打篮球"));
        shallowCopyClone.getDetail().setAddress("guangzhou");

        System.out.println("after shallowCopy = " + shallowCopy);
        System.out.println("after shallowCopyClone = " + shallowCopyClone);

        /*
         * 输出结果：
         * before shallowCopy = ShallowCopy(id=1001, name=浅拷贝, detail=Detail(phone=123456, hobby=[听音乐], address=shenzhen))
         * after shallowCopy = ShallowCopy(id=1001, name=浅拷贝, detail=Detail(phone=123457, hobby=[打篮球], address=guangzhou))
         * after shallowCopyClone = ShallowCopy(id=1002, name=浅拷贝克隆, detail=Detail(phone=123457, hobby=[打篮球], address=guangzhou))
         */
    }

    /**
     * 深拷贝
     */
    public static void deepCopyTest() throws CloneNotSupportedException {
        Detail detail = new Detail("123456", Collections.singletonList("听音乐"), "shenzhen");

        DeepCopy deepCopy = new DeepCopy(2001, "深拷贝", detail);
        System.out.println("before deepCopy = " + deepCopy);

        DeepCopy deepCopyClone = deepCopy.clone();
        deepCopyClone.setId(2002);
        deepCopyClone.setName("深拷贝克隆");
        // 这里更新克隆的shallowCopyClone引用对象的值，原型对象shallowCopy对应的引用对象的值不会被更改，
        // 浅克隆里它们指向的引用对象是不同的对象。
        deepCopyClone.getDetail().setPhone("123457");
        deepCopyClone.getDetail().setHobby(Collections.singletonList("打篮球"));
        deepCopyClone.getDetail().setAddress("guangzhou");

        System.out.println("after deepCopy = " + deepCopy);
        System.out.println("after deepCopyClone = " + deepCopyClone);

        /*
         * 输出结果：
         * before deepCopy = DeepCopy(id=2001, name=深拷贝, detail=Detail(phone=123456, hobby=[听音乐], address=shenzhen))
         * after deepCopy = DeepCopy(id=2001, name=深拷贝, detail=Detail(phone=123456, hobby=[听音乐], address=shenzhen))
         * after deepCopyClone = DeepCopy(id=2002, name=深拷贝克隆, detail=Detail(phone=123457, hobby=[打篮球], address=guangzhou))
         */
    }
}
