package com.lp.demo.action.java_in_action.函数式编程;


import com.lp.demo.common.util.ConsoleColorUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author lp
 * @date 2022/11/21 11:05
 * @desc 函数式接口的使用
 **/
public class JavaFunctionalInterfaceDemo {
    public static void main(String[] args) {
        ConsoleColorUtil.printDefaultColor("=================SupplierTest===================");
        SupplierTest.main(null);

        ConsoleColorUtil.printDefaultColor("=================ConsumerTest===================");
        ConsumerTest.main(null);

        ConsoleColorUtil.printDefaultColor("=================FunctionTest===================");
        FunctionTest.main(null);
        ConsoleColorUtil.printDefaultColor("=================FunctionTest2===================");
        FunctionTest2.main(null);

        ConsoleColorUtil.printDefaultColor("=================PredicateTest===================");
        PredicateTest.main(null);

    }

    /**
     * Supplier
     */
    static class SupplierTest {
        public static void main(String[] args) {
            SupplierTest supplierTest = new SupplierTest();
            String s = supplierTest.doSupplier(supplierTest::hasNoParam);
            System.out.println(s);

            String doSupplier = supplierTest.doSupplier(() -> "B-supplier");
            System.out.println(doSupplier);
        }

        public <T> T doSupplier(Supplier<T> supplier) {
            return supplier.get();
        }

        public String hasNoParam() {
            return "A-supplier";
        }
    }

    /**
     * Consumer
     */
    static class ConsumerTest {
        public static void main(String[] args) {
            doConsumer(Arrays.asList(1, 2, 3, 4, 5), System.out::println);
        }

        public static <T> void doConsumer(List<T> list, Consumer<T> c) {
//            list.forEach(c);
            for (T i : list) {
                c.accept(i);
            }
        }

    }

    /**
     * Function
     */
    static class FunctionTest {
        public static void main(String[] args) {
            FunctionTest functionTest = new FunctionTest();
            String s = functionTest.doFunction(functionTest::hasOneParam, "s");
            Integer integer = functionTest.doFunction(functionTest::increase, 6);
            System.out.println(s);
            System.out.println(integer);
        }

        public <T, R> R doFunction(Function<T, R> function, T param) {
            return function.apply(param);
        }

        public <T> String hasOneParam(T param) {
            return param.toString();
        }

        public Integer increase(Integer i) {
            return i + 1;
        }
    }

    static class FunctionTest2 {
        public static void main(String[] args) {
            FunctionTest2 functionTest2 = new FunctionTest2();
            String s = functionTest2.doFunction(FunctionTest2::hasNoParam, functionTest2);
            System.out.println(s);
        }

        public <T, R> R doFunction(Function<T, R> function, T param) {
            return function.apply(param);
        }

        public String hasNoParam() {
            return "A-function";
        }
    }

    /**
     * Predicate
     */
    static class PredicateTest {
        public static void main(String[] args) {
            List<String> nonEmpty = filter(Arrays.asList("1", "2", "", "3"), (String s) -> !s.isEmpty());
            System.out.println("nonEmpty = " + nonEmpty);
        }

        public static <T> List<T> filter(List<T> list, Predicate<T> p) {
            List<T> results = new ArrayList<>();
            for (T s : list) {
                if (p.test(s)) {
                    results.add(s);
                }
            }
            return results;
        }

    }


}
