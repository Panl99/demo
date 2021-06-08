package com.lp.demo.common.designpatterns.singletonpattern;

/**
 * @create 2020/9/17 21:27
 * @auther outman
 **/
public class Singleton {
    /**
     * 静态内部类模式
     * 线程安全、效率高、可延时加载
     * ** 推荐 **
     */
    public static class Singleton3 {
        private Singleton3() {}
        private static class SingletonInstance {
            private static final Singleton3 INSTANCE = new Singleton3();
        }
        public static Singleton3 getInstance() {
            return SingletonInstance.INSTANCE;
        }
    }

    /**
     * 枚举
     * 枚举元素本身就是单例
     * 线程安全、效率高、不能延时加载、可以防反射 反序列化调用
     */
    public enum Singleton4 {
        INSTANCE;

        public void singletonOperation() {
            //do something
        }
    }

    /**
     * 懒汉模式
     * 只适用于单线程
     */
    public static class Singleton1 {
        private Singleton1() {}
        private static Singleton1 instance = null;
        public static Singleton1 getInstance() {
            if (instance == null) {
                instance = new Singleton1();
            }
            return instance;
        }
    }

    /**
     * 饿汉模式
     * 不能延迟加载
     * 初始化时就加载好实例，要是到头来不用，浪费
     */
    public static class Singleton2 {
        private Singleton2() {}
        private static Singleton2 instance = new Singleton2();
        public static Singleton2 getInstance() {
            return instance;
        }
    }

}
