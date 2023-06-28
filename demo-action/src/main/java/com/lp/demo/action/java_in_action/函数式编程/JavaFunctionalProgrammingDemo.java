package com.lp.demo.action.java_in_action.函数式编程;


import com.google.common.collect.Lists;
import com.lp.demo.common.util.ConsoleColorUtil;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author lp
 * @date 2022/11/21 11:05
 * @desc 函数式编程
 **/
public class JavaFunctionalProgrammingDemo {
    public static void main(String[] args) {
        ConsoleColorUtil.printDefaultColor("=================StreamGroupByTest===================");
        StreamGroupByTest.main(null);

        ConsoleColorUtil.printDefaultColor("=================StreamPeekTest===================");
        StreamPeekTest.main(null);

    }

    /**
     * Collectors.groupingBy例子
     *
     * groupBy有三种用法：
     *
     * groupingBy(Function) – 单纯分key存放成Map，默认使用HashMap
     * groupingBy(Function, Collector) - 分key后，对每个key的元素进行后续collect操作，其中Collector还可以继续进行groupingBy操作，进行无限往下分类
     * groupingBy(Function, Supplier, Collector) - 同上，允许自定义Map创建
     *
     * collect函数提供了两种参数类型
     * collect(Supplier, Accumulator, Combiner)
     * collect(Collector)
     *
     * collect是汇合Stream元素的操作，在已经拥有了reduce函数，为什么还需要collect函数？
     * reduce操作不可变数据
     * collect操作可变数据
     */
    static class StreamGroupByTest {
        @Data
        @Builder
        public static class Programmer {
            private String name;
            private int level;
            private int salary;
            private int output; //from 1-10
            private String language;
        }

        static class Tuple<L, R> {
            private final L l;
            private final R r;

            public Tuple(L l, R r) {
                this.l = l;
                this.r = r;
            }

            @Override
            public String toString() {
                return "(" + l + ", " + r + " )";
            }
        }

        public static Map<String, Map<Integer, Tuple<Integer, List<Programmer>>>> classify(List<Programmer> programmers) {
            return programmers.stream().collect(
                    // 首先把程序员语言语言分类
                    Collectors.groupingBy(Programmer::getLanguage,
                            // 再这里已经通过语言分好的类后，可以继续使用groupingBy对同一语言的不同等级程序员进行分类
                            Collectors.groupingBy(Programmer::getLevel,
                                    // 这里collectingAndThen主要接受两个参数，一个是Collector，第二个是Collector完成后进行一个附加操作，这里就是把同一等级的程序员的工资进行一个平均
                                    Collectors.collectingAndThen(
                                            Collectors.toList(),
                                            // 此处的list就已经是同一语言同一等级的程序员list，再对该list进行求工资平均值操作
                                            list -> new Tuple(list.stream().collect(Collectors.averagingInt(Programmer::getSalary)), list)
                                    )
                            )
                    ));
        }

        public static void main(String[] args) {
            List<Programmer> programmers = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                Programmer programmer = new Programmer(
                        "name-" + i,
                        ThreadLocalRandom.current().nextInt(1, 5),
                        ThreadLocalRandom.current().nextInt(3, 5) * 1000,
                        i,
                        i % 3 == 0 ? "javascript" :
                        i % 2 == 0 ? "java" : "go");
                programmers.add(programmer);
            }
            Map<String, Map<Integer, Tuple<Integer, List<Programmer>>>> classify = classify(programmers);
            classify.entrySet().forEach(System.out::println);
            /**
             * java={
             * 1=(3000.0, [JavaFunctionalInterfaceDemo.StreamGroupByTest.Programmer(name=name-8, level=1, salary=3000, output=8, language=java)] ),
             * 2=(3500.0, [JavaFunctionalInterfaceDemo.StreamGroupByTest.Programmer(name=name-2, level=2, salary=4000, output=2, language=java),
         *                 JavaFunctionalInterfaceDemo.StreamGroupByTest.Programmer(name=name-4, level=2, salary=3000, output=4, language=java)] ),
             * 4=(4000.0, [JavaFunctionalInterfaceDemo.StreamGroupByTest.Programmer(name=name-10, level=4, salary=4000, output=10, language=java)] )
             * }
             *
             * go={
             * 1=(3000.0, [JavaFunctionalInterfaceDemo.StreamGroupByTest.Programmer(name=name-7, level=1, salary=3000, output=7, language=go)] ),
             * 2=(3000.0, [JavaFunctionalInterfaceDemo.StreamGroupByTest.Programmer(name=name-1, level=2, salary=3000, output=1, language=go)] ),
             * 3=(4000.0, [JavaFunctionalInterfaceDemo.StreamGroupByTest.Programmer(name=name-5, level=3, salary=4000, output=5, language=go)] )
             * }
             *
             * javascript={
             * 1=(4000.0, [JavaFunctionalInterfaceDemo.StreamGroupByTest.Programmer(name=name-3, level=1, salary=4000, output=3, language=javascript)] ),
             * 2=(4000.0, [JavaFunctionalInterfaceDemo.StreamGroupByTest.Programmer(name=name-6, level=2, salary=4000, output=6, language=javascript)] ),
             * 4=(4000.0, [JavaFunctionalInterfaceDemo.StreamGroupByTest.Programmer(name=name-9, level=4, salary=4000, output=9, language=javascript)] )
             * }
             */
        }
    }

    static class StreamPeekTest {
        public static void main(String[] args) {
            List<String> newList = Stream.of("one", "two", "three", "four")
                    .filter(e -> e.length() > 3)
                    .peek(e -> System.out.println("Filtered value: " + e))
                    .map(String::toUpperCase)
                    .peek(e -> System.out.println("Mapped value: " + e))
                    .collect(Collectors.toList());
            System.out.println("newList = " + newList);
            /**
             * Filtered value: three
             * Mapped value: THREE
             * Filtered value: four
             * Mapped value: FOUR
             * newList = [THREE, FOUR]
             */
        }
    }

    /**
     * 列表内容分批处理示例
     * 类似：guava的Lists的partition
     * 使用场景：批量导入
     */
    static class StreamSkipTest {
        public static void main(String[] args) {
            List<Integer> list = Arrays.asList(11,33,55,77,99,22,66,88);
            int pageSize = 3; // 每次处理条数
            int pages = (int) Math.ceil(list.size() * 1.0 / pageSize); // 计算要分几次处理

            for (int i = 1; i <= pages; i++) {
                List<Integer> collect = list.stream().skip((i - 1) * pageSize).limit(pageSize).collect(Collectors.toList());
                System.out.println("collect-" + i +" = " + collect);
            }

            List<List<Integer>> partition = Lists.partition(list, pageSize);
            System.out.println("partition = " + partition);
            /**
             * 结果：
             * collect-1 = [11, 33, 55]
             * collect-2 = [77, 99, 22]
             * collect-3 = [66, 88]
             *
             * partition = [[11, 33, 55], [77, 99, 22], [66, 88]]
             */
        }
    }

}
