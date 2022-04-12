package com.lp.demo.common.util;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.google.common.base.Stopwatch;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author lp
 * @date 2021/12/1 10:26
 **/
public class BetterReflect {
    /**
     * 反射核心的是getMethod和invoke，这两者在原生调用下很耗时。
     * 1. 优化getMethod：缓存Method，不重复调用。（缓存Method比直接getMethod大概快38倍，离直接调用还差个9倍）
     * 2. 优化invoke：使用reflectAsm，让invoke变成直接调用
     *   - invoke不像getMethod可以缓存起来重复用，没法优化，因此引入ASM。
     *   - invoke是没办法优化的，也没办法做到像直接调用那么快。所以大佬们脑洞大开，不用反射的invoke了。原理如下：
     *     借反射的getDeclaredMethods获取SimpleBean.class的所有方法，然后动态生成一个继承于MethodAccess 的子类SimpleBeanMethodAccess，动态生成一个Class文件并load到JVM中。
     *     SimpleBeanMethodAccess中所有方法名建立index索引，index跟方法名是映射的，根据方法名获得index，SimpleBeanMethodAccess内部建立的switch直接分发执行相应的代码，这样methodAccess.invoke的时候，实际上是直接调用。
     *   - 实际上reflectAsm是有个致命漏洞的，因为要生成文件，还得load进JVM，所以reflectAsm的getMethod特别慢
     *  - TODO 测试好像并没有快？？
     */

    private BetterReflect() {}
    private static class SingletonInstance {
        private static final BetterReflect INSTANCE = new BetterReflect();
    }
    public static BetterReflect getInstance() {
        return SingletonInstance.INSTANCE;
    }

    private static final long times = 100000000;
    private static final String formatter = "betterReflect-->%s,%d,%d";

    //通过Java Class类自带的反射获得Method测试，仅进行一次method获取
    public static void javaReflectGetOnly() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Method getName = BetterReflect.class.getMethod("getName");
        Stopwatch watch = Stopwatch.createStarted();
        for (long i = 0; i < times; i++) {
            getName.invoke(getInstance());
        }
        watch.stop();
        String result = String.format(formatter, "原生反射+缓存Method", times, watch.elapsed(TimeUnit.MILLISECONDS));
        System.out.println(result);
        // 1亿次：195 166 167 168
        // UserDto.init 5986 5676 5892
    }

    public static void getName() {
//        ConsoleColorUtil.printDefaultColor(">>>>>>>>>>call getName<<<<<<<<<<<<");
    }

    //通过高性能的ReflectAsm库进行测试，仅进行一次methodAccess获取
    public static void reflectAsmGetOnly() {
        MethodAccess methodAccess = MethodAccess.get(BetterReflect.class);
        Stopwatch watch = Stopwatch.createStarted();
        for (long i = 0; i < times; i++) {
            methodAccess.invoke(getInstance(), "getName");
        }
        watch.stop();
        String result = String.format(formatter, "reflectAsm反射优化+缓存Method", times, watch.elapsed(TimeUnit.MILLISECONDS));
        System.out.println(result);
        // 1亿次：817 804 796 785 986
        // UserDto.init 6651 7066 6203
    }

    // 直接调用
    public static void callGetName() {
        Stopwatch watch = Stopwatch.createStarted();
        for (int i = 0; i < times; i++) {
            getName();
        }
        watch.stop();
        String result = String.format(formatter, "直接调用", times, watch.elapsed(TimeUnit.MILLISECONDS));
        System.out.println(result);
        // 1亿次：44 45 46 48 48
        // UserDto.init 5377 5091 5263
    }


    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        javaReflectGetOnly();
//        reflectAsmGetOnly();
//        callGetName();
    }

}
