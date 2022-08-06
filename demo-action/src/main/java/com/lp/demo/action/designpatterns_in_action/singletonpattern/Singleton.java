package com.lp.demo.action.designpatterns_in_action.singletonpattern;

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
     * 1.懒汉模式：只适用于单线程
     * 问题：多线程中：A线程执行代码1的同时，B线程执行代码2，线程A可能会看到对象没有初始化 导致线程安全问题。
     */
    public static class Singleton1 {
        private static Singleton1 instance;
        public static Singleton1 getInstance() {
            if (instance == null) {                 // 1：A线程执行
                instance = new Singleton1();        // 2：B线程执行
            }
            return instance;
        }
    }
    /**
     * 2.懒汉模式：延迟处理，通过synchronized同步处理实现线程安全。
     * 问题：但是会导致性能下降。不频繁调用getInstance()时可用。
     */
    public static class Singleton11 {
        private static Singleton11 instance;
        public synchronized static Singleton11 getInstance() { // 同步处理
            if (instance == null) {
                instance = new Singleton11();
            }
            return instance;
        }
    }
    /**
     * 3.懒汉模式：双重检查锁，降低第2种方式的同步开销。
     * 问题：跟指令重排序有关，当线程B执行到第1步时，代码读取到instance不为null时，instance引用的对象可能还没有完成初始化。
     * 第4步创建了一个对象，创建一个对象可分解为：①分配对象的内存空间，②初始化对象，③设置instance指向内存空间，④访问instance引用的对象
     * 在某些JIT编译器上，重排序是真实发生的，比如步骤②、步骤③可能重排序了
     * Java语言规范会保证在单线程内，不改变程序执行结果的重排序，但是线程B在访问第1步检查时instance不为null，线程B接下来将访问instance所引用的对象，但此时这个对象可能还没有被线程A初始化
     */
    public static class Singleton111 {
        private static Singleton111 instance;
        public static Singleton111 getInstance() {
            if (instance == null) {                        // 1：第一次检查
                synchronized (Singleton111.class) {        // 2：加锁
                    if (instance == null) {                // 3：第二次检查
                        instance = new Singleton111();     // 4：问题的根源在这里
                    }
                }
            }
            return instance;
        }
    }
    /**
     * 4.懒汉模式：双重检查锁，线程安全方式，最终写法
     * 使用volatile保证instance对多线程的可见，和防止指令中排序(解决方式3)
     */
    public static class Singleton1111 {
        private volatile static Singleton1111 instance; // volatile
        public static Singleton1111 getInstance() {
            if (instance == null) {
                synchronized (Singleton1111.class) {
                    if (instance == null) {
                        instance = new Singleton1111();
                    }
                }
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
