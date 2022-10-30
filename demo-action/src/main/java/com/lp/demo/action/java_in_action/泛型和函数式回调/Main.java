package com.lp.demo.action.java_in_action.泛型和函数式回调;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lp
 * @date 2022/10/30 23:17
 * @desc
 **/
public class Main {
    public static void main(String[] args) {
        // 处理异常
        Map<String, String> map = ConcurrentStartUtil.start(20,
                () -> {
                    if (1 == 1) {
                        throw new RuntimeException("测试异常!");
                    }
                    return "333";
                },
                (e) -> {
                    String message = e.getMessage();
                    System.out.println("message = " + message);
                });
        System.out.println("map = " + map);

        // 不处理异常
        Map<String, String> map2 = ConcurrentStartUtil.start(20,
                () -> {
                    if (1 == 1) {
                        throw new RuntimeException("测试异常!");
                    }
                    return "555";
                });
        System.out.println("map2 = " + map2);


        /**
         * param：A,B；
         * return：C
         */

        // 处理异常
        Map<String, String> map3 = ConcurrentStartUtil2.start(20,
                (a, b) -> {
                    if (1 == 1) {
                        throw new RuntimeException("测试异常!");
                    }
                    return a + b;
                },
                "a", // 这里可以传输任何类型，比较灵活
                "b", // 这里可以传输任何类型，比较灵活
                (e) -> {
                    String message = e.getMessage();
                    System.out.println("message = " + message);
                });
        System.out.println("map3 = " + map3);

        // 不处理异常
        Map<String, List<String>> map4 = ConcurrentStartUtil2.start(20,
                (a, b) -> {
                    b.add(a);
                    b.add("bbbb");
                    return b;
                },
                "a", // 这里可以传输任何类型，比较灵活
                new ArrayList<String>() // 这里可以传输任何类型，比较灵活
        );
        System.out.println("map4 = " + map4);
    }
}
