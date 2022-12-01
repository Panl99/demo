package com.lp.demo.action.spring_in_action.spring_test;

import com.lp.demo.common.util.ConsoleColorUtil;
import org.springframework.data.util.Lazy;

/**
 * @author lp
 * @date 2022/11/30 15:41
 * @desc
 **/
public class SpringUtil_LazyTest {

    public static void main(String[] args) {
        ConsoleColorUtil.printDefaultColor("=================_1Test===================");
        _1Test();
        ConsoleColorUtil.printDefaultColor("=================_2Test===================");
        _2Test();
        ConsoleColorUtil.printDefaultColor("=================_3Test===================");
        _3Test();

    }

    public static void _1Test() {
        Lazy<Integer> a = Lazy.of(() -> 10 + 1);
        // a的计算在这里才完成
        int b = a.get() + 1;
        System.out.println("b = " + b);
        // get 不会再重新计算, 直接用缓存的值
        int c = a.get();
        System.out.println("c = " + c);
    }

    public static void _2Test() {
        Lazy<Object> lazy = Lazy.of(() -> null).or("333");
        System.out.println("lazy.get() = " + lazy.get()); // 333

        Object obj = Lazy.of(() -> null).orElse("555");
        System.out.println("obj = " + obj); // 555
    }

    public static void _3Test() {
        Integer v = Lazy.of("777")
                        .map(Integer::parseInt)
                        .get();
        System.out.println("v = " + v);

        Lazy<Long> param1Lazy = Lazy.of(() -> 2L);
        Lazy<Long> param2Lazy = Lazy.of(() -> 3L);
        Lazy<Long> param3Lazy = Lazy.of(() -> 5L);
        Lazy<Long> result = param1Lazy.flatMap(param1 ->
                            param2Lazy.flatMap(param2 ->
                            param3Lazy.map(param3 -> param1 + param2 + param3)
                            )
        );
        System.out.println("result = " + result.get());
    }
}
