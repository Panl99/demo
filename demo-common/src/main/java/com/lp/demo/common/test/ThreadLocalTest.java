package com.lp.demo.common.test;

import com.lp.demo.common.aop.thread.ThreadPoolUtil;
import com.lp.demo.common.util.ConsoleColorUtil;

/**
 * @author lp
 * @date 2021/9/11 10:50
 **/
public class ThreadLocalTest {

    static ThreadLocal<String> localVar = new ThreadLocal<>();

    static void print(String str) {
        //打印当前线程中本地内存中本地变量的值
        ConsoleColorUtil.printDefaultColor(str + ": " + localVar.get());
        //清除本地内存中的本地变量
        localVar.remove();
    }

    public static void main(String[] args) {
        ThreadPoolUtil.submitTask(() -> {
            //设置线程1中本地变量的值
            localVar.set("localVar1");
            //调用打印方法
            print("thread1");
            //打印本地变量
            ConsoleColorUtil.printDefaultColor("after remove thread1: " + localVar.get());
        });
        ThreadPoolUtil.submitTask(() -> {
            //设置线程1中本地变量的值
            localVar.set("localVar2");
            //调用打印方法
            print("thread2");
            //打印本地变量
            ConsoleColorUtil.printDefaultColor("after remove thread2: " + localVar.get());
        });
//        Thread t1 = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                //设置线程1中本地变量的值
//                localVar.set("localVar1");
//                //调用打印方法
//                print("thread1");
//                //打印本地变量
//                ConsoleColorUtil.printDefaultColor("after remove thread1: " + localVar.get());
//            }
//        });
//
//        Thread t2 = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                //设置线程1中本地变量的值
//                localVar.set("localVar2");
//                //调用打印方法
//                print("thread2");
//                //打印本地变量
//                ConsoleColorUtil.printDefaultColor("after remove thread2: " + localVar.get());
//            }
//        });
//
//        t1.start();
//        t2.start();
    }
}
