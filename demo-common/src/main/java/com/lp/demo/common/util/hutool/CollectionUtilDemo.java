package com.lp.demo.common.util.hutool;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.ListUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author lp
 * @date 2024/5/16 16:14
 * @desc
 **/
public class CollectionUtilDemo {


    private static final List<Integer> LIST_1 = CollectionUtil.toList(1, 2, 3, 4, 5, 6);
    private static final List<Integer> LIST_2 = CollectionUtil.toList(1, 2, 3, 2, 5, 7);
    private static final List<Integer> LIST_3 = CollectionUtil.toList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    private static final Map<Integer, String> MAP_1 = new HashMap<Integer, String>() {
        {
            put(1, "one");
            put(2, "two");
            put(3, "three");
            put(4, "four");
            put(7, "seven");
        }
    };
    private static final Map<Integer, List<Integer>> MAP_2 = new HashMap<Integer, List<Integer>>() {
        {
            put(1, Arrays.asList(1, 10, 11));
            put(2, Arrays.asList(2, 20, 21));
            put(3, Arrays.asList(3, 30, 31));
            put(4, Arrays.asList(4, 40, 41));
            put(7, Arrays.asList(7, 70, 71));
        }
    };
    private static final Map<Integer, List<String>> MAP_3 = new HashMap<Integer, List<String>>() {
        {
            put(1, Arrays.asList("apple", "apricot", "banana", "blueberry", "cherry"));
            put(2, Arrays.asList("carrot", "cabbage", "bean", "beet", "corn"));
        }
    };


    static class ListDemo {

        public static void main(String[] args) {
            System.out.println(" ----------------------------List test1-------------------------- ");
            test1();
            System.out.println(" ----------------------------List test2-------------------------- ");
            test2();
            System.out.println(" ----------------------------List test3-------------------------- ");
            test3();
            System.out.println(" ----------------------------List test4-------------------------- ");
            test4();
            System.out.println(" ----------------------------List test5-------------------------- ");
            test5();

        }

        public static void test1() {
            // 集合差集
            // L1 - L2
            List<Integer> subtract1 = CollectionUtil.subtractToList(LIST_1, LIST_2);
            System.out.println("subtract1 = " + subtract1); // subtract1 = [4, 6]
            // L2 - L1
            List<Integer> subtract2 = CollectionUtil.subtractToList(LIST_2, LIST_1);
            System.out.println("subtract2 = " + subtract2); // subtract2 = [7]

            // 集合交集
            Collection<Integer> commonItems = CollectionUtil.intersection(LIST_1, LIST_2);
            System.out.println("commonItems = " + commonItems); // commonItems = [1, 2, 3, 5]
            // 去重
            Collection<Integer> commonItemsDistinct = CollectionUtil.intersectionDistinct(LIST_1, LIST_2);
            System.out.println("commonItemsDistinct = " + commonItemsDistinct); // commonItemsDistinct = [1, 2, 3, 5]

            // 集合并集
            Collection<Integer> union = CollectionUtil.union(LIST_1, LIST_2);
            System.out.println("union = " + union); // union = [1, 2, 2, 3, 4, 5, 6, 7] // ？？
            Collection<Integer> unionDistinct = CollectionUtil.unionDistinct(LIST_1, LIST_2);
            System.out.println("unionDistinct = " + unionDistinct); // unionDistinct = [1, 2, 3, 4, 5, 6, 7]

            // 集合是否包含某些元素
            // 包含全部
            boolean b1 = CollectionUtil.containsAll(LIST_1, Arrays.asList(1, 5, 6));
            System.out.println("b1 = " + b1); // b1 = true
            boolean b2 = CollectionUtil.containsAll(LIST_1, Arrays.asList(1, 5, 7));
            System.out.println("b2 = " + b2); // b2 = false
            // 包含任一个
            boolean b3 = CollectionUtil.containsAny(LIST_1, Arrays.asList(1, 5, 7));
            System.out.println("b3 = " + b3); // b3 = true

            // 集合转字符串
            String toStr = CollectionUtil.join(LIST_1, ",");
            System.out.println("toStr = " + toStr); // toStr = 1,2,3,4,5,6

        }

        // 合并列表
        public static void test2() {
            List<Integer> collect = Stream.of(LIST_1, LIST_2).flatMap(Collection::stream).collect(Collectors.toList());
            System.out.println("collect = " + collect); // collect = [1, 2, 3, 4, 5, 6, 1, 2, 3, 2, 5, 7]

            List<Integer> union = ListUtils.union(LIST_1, LIST_2);
            System.out.println("union = " + union); // union = [1, 2, 3, 4, 5, 6, 1, 2, 3, 2, 5, 7]

            List<Integer> reduce = Stream.of(LIST_1, LIST_2).reduce(new ArrayList<>(), (a, b) -> {
                a.addAll(b);
                return a;
            });
            System.out.println("reduce = " + reduce); // reduce = [1, 2, 3, 4, 5, 6, 1, 2, 3, 2, 5, 7]
        }

        /**
         * 列表分成均等多份
         */
        public static void test3() {
            // 这里3指的每份分3个
            List<List<Integer>> partition3 = ListUtils.partition(LIST_3, 3);
            System.out.println("partition3 = " + partition3); // partition3 = [[1, 2, 3], [4, 5, 6], [7, 8, 9], [10]]
            List<List<Integer>> partition4 = ListUtils.partition(LIST_3, 4);
            System.out.println("partition4 = " + partition4); // partition4 = [[1, 2, 3, 4], [5, 6, 7, 8], [9, 10]]

            List<List<Integer>> lists_3 = partitionList(LIST_3, 3);
            System.out.println("lists_3 = " + lists_3);
            List<List<Integer>> lists_4 = partitionList(LIST_3, 4);
            System.out.println("lists_4 = " + lists_4);
        }

        public static List<List<Integer>> partitionList(List<Integer> list, int n) {
            if (n <= 0) {
                throw new IllegalArgumentException("Partition size should be greater than zero.");
            }
            return IntStream.range(0, (list.size() + n - 1) / n)
                    .mapToObj(i -> list.subList(i * n, Math.min(list.size(), (i + 1) * n)))
                    .collect(Collectors.toList());
        }

        /**
         * 列表分成n份，非均等
         */
        public static void test4() {
            List<List<Integer>> averageAssign3 = averageAssign(LIST_3, 3);
            System.out.println("averageAssign3 = " + averageAssign3); // averageAssign3 = [[1, 2, 3, 4], [5, 6, 7], [8, 9, 10]]
            List<List<Integer>> averageAssign4 = averageAssign(LIST_3, 4);
            System.out.println("averageAssign4 = " + averageAssign4); // averageAssign4 = [[1, 2, 3], [4, 5, 6], [7, 8], [9, 10]]
            List<List<Integer>> averageAssign5 = averageAssign(LIST_3, 5);
            System.out.println("averageAssign5 = " + averageAssign5); // averageAssign5 = [[1, 2], [3, 4], [5, 6], [7, 8], [9, 10]]

        }

        /**
         * 将一个list均分成n个list,主要通过偏移量来实现的
         */
        public static <T> List<List<T>> averageAssign(List<T> source, int n) {
            if (CollectionUtil.isEmpty(source)) {
                return Collections.emptyList();
            }
            List<List<T>> result = new ArrayList<>();
            int remaider = source.size() % n; //(先计算出余数)
            int number = source.size() / n; //然后是商
            int offset = 0;//偏移量
            for (int i = 0; i < n; i++) {
                List<T> value;
                if (remaider > 0) {
                    value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                    remaider--;
                    offset++;
                } else {
                    value = source.subList(i * number + offset, (i + 1) * number + offset);
                }
                result.add(value);
            }
            return result;
        }

        /**
         * Java 给定两个相同长度为3的List<List<Integer>> list1和List<List<Integer>> list2，
         * 比较list1和list2同一级子列表中元素是否存在相同项，若存在相同项，则在list1的下个子列表中找到一个不同项与之进行交换，
         * 最终使得list1和list2对应的同级子列表不能存在相同项
         */
            public static void test5() {
                List<List<Integer>> list1 = new ArrayList<>();
                List<List<Integer>> list2 = new ArrayList<>();

                // 初始化示例数据
//            list1.add(new ArrayList<>(Arrays.asList(1, 2, 3)));
//            list1.add(new ArrayList<>(Arrays.asList(4, 5, 6)));
//            list1.add(new ArrayList<>(Arrays.asList(7, 8, 9)));
//
//            list2.add(new ArrayList<>(Arrays.asList(6, 4, 1)));
//            list2.add(new ArrayList<>(Arrays.asList(3, 5, 7)));
//            list2.add(new ArrayList<>(Arrays.asList(9, 8, 2)));
                // [[8, 2, 9], [4, 1, 6], [7, 5, 3]]
                // [[6, 4, 1], [3, 5, 7], [9, 8, 2]]

//            list1.add(new ArrayList<>(Arrays.asList(1, 2, 3)));
//            list1.add(new ArrayList<>(Arrays.asList(4, 5, 6)));
//            list1.add(new ArrayList<>(Arrays.asList(7, 8, 9)));
//
//            list2.add(new ArrayList<>(Arrays.asList(6, 8, 1)));
//            list2.add(new ArrayList<>(Arrays.asList(5, 2, 7)));
//            list2.add(new ArrayList<>(Arrays.asList(9, 4, 3)));
                // [[4, 9, 3], [1, 8, 6], [7, 5, 2]]
                // [[6, 8, 1], [5, 2, 7], [9, 4, 3]]

//            list1.add(new ArrayList<>(Arrays.asList(1, 3)));
//            list1.add(new ArrayList<>(Arrays.asList(4, 5, 2)));
//            list1.add(new ArrayList<>(Arrays.asList(7, 8)));
//
//            list2.add(new ArrayList<>(Arrays.asList(6, 8, 1)));
//            list2.add(new ArrayList<>(Arrays.asList(5, 2, 7)));
//            list2.add(new ArrayList<>(Arrays.asList(9, 4, 3)));
                // [[5, 2], [1, 4, 3], [7, 8]]
                // [[6, 8, 1], [5, 2, 7], [9, 4, 3]]

//            list1.add(new ArrayList<>(Arrays.asList(1, 3, 6)));
//            list1.add(new ArrayList<>(Arrays.asList(4, 5, 2)));
//            list1.add(new ArrayList<>(Arrays.asList(7, 8)));
//
//            list2.add(new ArrayList<>(Arrays.asList(6, 8, 1)));
//            list2.add(new ArrayList<>(Arrays.asList(5, 2, 3)));
//            list2.add(new ArrayList<>(Arrays.asList(9, 4, 7)));
                // [[7, 3, 5], [1, 6, 4], [2, 8]]
                // [[6, 8, 1], [5, 2, 3], [9, 4, 7]]

                // 打印初始列表
                System.out.println("Original List 1: " + list1);
                System.out.println("Original List 2: " + list2);

                swapElementsToAvoidDuplicates(list1, list2);

                // 打印处理后的列表
                System.out.println("Processed List 1: " + list1);
                System.out.println("Processed List 2: " + list2);
            }

            public static void swapElementsToAvoidDuplicates(List<List<Integer>> list1, List<List<Integer>> list2) {
                int size = list1.size();
                for (int i = 0; i < size; i++) {
                    List<Integer> sublist1 = list1.get(i);
                    List<Integer> sublist2 = list2.get(i);

                    Set<Integer> set2 = new HashSet<>(sublist2);

                    for (int j = 0; j < sublist1.size(); j++) {
                        if (set2.contains(sublist1.get(j))) {
                            boolean swapped = false;

                            // 尝试在下一个子列表中找到一个不同的项进行交换
//                        if (i + 1 < size) {
//                            List<Integer> nextSublist = list1.get(i + 1);
//                            for (int k = 0; k < nextSublist.size(); k++) {
//                                if (!set2.contains(nextSublist.get(k))) {
//                                    // 进行交换
//                                    int temp = sublist1.get(j);
//                                    sublist1.set(j, nextSublist.get(k));
//                                    nextSublist.set(k, temp);
//                                    swapped = true;
//                                    break;
//                                }
//                            }
//                        }
                            int flag = 0;
                            while (!swapped && flag < size) {
                                if (flag == i) {
                                    flag++;
                                    continue;
                                }
                                List<Integer> nextSublist = list1.get(flag);

//                            if (i + 1 < size) {
//                                nextSublist = list1.get(i + 1);
//                            } else /*if (i + 1 == size)*/ {
//                                nextSublist = list1.get(0);
//                            }
                                for (int k = 0; k < nextSublist.size(); k++) {
                                    if (!set2.contains(nextSublist.get(k))) {
                                        // 进行交换
                                        int temp = sublist1.get(j);
                                        sublist1.set(j, nextSublist.get(k));
                                        nextSublist.set(k, temp);
                                        swapped = true;
                                        break;
                                    }
                                }
                                flag++;
                            }

                            // 如果没有找到可以交换的元素，打印错误消息
                            if (!swapped) {
                                System.out.println("Error: Cannot find a suitable element to swap.");
                                return;
                            }
                        }
                    }
                }
            }


    }







    static class MapDemo{
        public static void main(String[] args) {
            System.out.println(" ----------------------------Map test1-------------------------- ");
            test1();
            System.out.println(" ----------------------------Map test2-------------------------- ");
            test2();
            System.out.println(" ----------------------------Map test3-------------------------- ");
            test3();
            System.out.println(" ----------------------------Map test4-------------------------- ");
            test4();
            System.out.println(" ----------------------------Map test5-------------------------- ");
            test5();
            System.out.println(" ----------------------------Map test6-------------------------- ");
            test6();

            System.out.println(" ----------------------------Map test8-------------------------- ");
            test8();



        }

        // Map<Long, List<?>>按key过滤后，合并value列表
        public static void test1() {
            List<Integer> reduce = MAP_2.entrySet().stream()
                    .filter(entry -> LIST_1.contains(entry.getKey()))
                    .map(Map.Entry::getValue)
                    .reduce(new ArrayList<>(), (a, b) -> {
                        a.addAll(b);
                        return a;
                    });
            System.out.println("reduce = " + reduce); // reduce = [1, 10, 11, 2, 20, 21, 3, 30, 31, 4, 40, 41]

            List<Integer> collect = MAP_2.entrySet().stream()
                    .filter(entry -> LIST_1.contains(entry.getKey()))
                    .map(Map.Entry::getValue)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
            System.out.println("collect = " + collect); // collect = [1, 10, 11, 2, 20, 21, 3, 30, 31, 4, 40, 41]

            List<Integer> list = MAP_2.entrySet().stream()
                    .filter(entry -> LIST_1.contains(entry.getKey()))
                    .map(Map.Entry::getValue)
                    .reduce(ListUtils::union)
                    .get();
            System.out.println("list = " + list); // list = [1, 10, 11, 2, 20, 21, 3, 30, 31, 4, 40, 41]
        }

        // Map<Long, List<?>>再分组
        public static void test2() {
            Map<Object, Map<Object, List<Object>>> groupedMap = MAP_2.entrySet().stream()
                    .collect(Collectors.groupingBy(
                            entry -> entry.getValue().get(1), // 假设按value中某项分组
                            Collectors.groupingBy(Map.Entry::getKey, Collectors.mapping(Map.Entry::getValue, Collectors.toList()))
                    ));
            System.out.println("groupedMap: " + JSONObject.parseObject(JSONObject.toJSONString(groupedMap)));
            // groupedMap: {"70":{"7":[[7,70,71]]},"40":{"4":[[4,40,41]]},"30":{"3":[[3,30,31]]},"20":{"2":[[2,20,21]]},"10":{"1":[[1,10,11]]}}
        }

        // Map<Long, List<?>>按值条件再分组
        public static void test3() {
            // 进行分组后的 Map
            Map<Integer, Map<String, List<String>>> groupedMap = MAP_3.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> entry.getValue().stream()
                                    .collect(Collectors.groupingBy(
                                            s -> s.substring(0, 1).toUpperCase()
                                    ))
                    ));

            // 输出结果
            System.out.println(JSONObject.parseObject(JSONObject.toJSONString(groupedMap)));
            // {"1":{"A":["apple","apricot"],"B":["banana","blueberry"],"C":["cherry"]},"2":{"B":["bean","beet"],"C":["carrot","cabbage","corn"]}}
        }

        // Map<Integer, Long> 转为 Map<Integer, Double>, 对value计算比值
        public static void test4() {
            Map<Integer, Long> map = new HashMap<>();
            map.put(2, 200L);
            map.put(1, 100L);
            map.put(3, 300L);
            map.put(44, 400L);

            long totalSum = map.values().stream().mapToLong(Long::longValue).sum();
            Map<Integer, Double> ratioMap = map.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey(Comparator.naturalOrder()))
                    .collect(HashMap::new,
                            (m, e) -> m.put(e.getKey(), Double.parseDouble(String.format("%.4f", (double) e.getValue() / totalSum))),
                            Map::putAll);
            System.out.println(JSONObject.parseObject(JSONObject.toJSONString(ratioMap)));
//            // {"1":0.1667,"2":0.3333,"3":0.5}

            // 前几项的累加和
            Set<Integer> keySet = map.keySet();
//            keySet.remove(44);
            Map<Integer, Double> resultMap = new HashMap<>();
            for (Integer i : keySet) {
                long sum = map.entrySet().stream()
                        .filter(m -> m.getKey() <= i)
                        .map(Map.Entry::getValue)
                        .mapToLong(Long::longValue)
                        .sum();
                resultMap.put(i, Double.parseDouble(String.format("%.4f", (double) sum / totalSum)));
            }
            System.out.println(JSONObject.parseObject(JSONObject.toJSONString(resultMap)));
            // {"44":1.0,"1":0.1,"2":0.3,"3":0.6}
        }

        // 遍历最外层的Map，并对内层Map中相同键值的Integer项求平均值，然后将结果转换为一个新的Map。
        public static void test5() {
            Map<String, Map<Integer, Double>> originalMap = new HashMap<>();
            originalMap.put("aaa", new HashMap<Integer, Double>(){{
                put(0, 0.3);
                put(1, 0.4);
                put(2, 0.5);
                put(3, 0.6);
            }});
            originalMap.put("bbb", new HashMap<Integer, Double>(){{
                put(0, 0.4);
                put(1, 0.5);
                put(2, 0.6);
                put(3, 0.7);
            }});

            Map<Integer, Double> averagedMap = originalMap.values().stream()
                    .flatMap(innerMap -> innerMap.entrySet().stream())
                    .collect(Collectors.groupingBy(
                            Map.Entry::getKey,
                            Collectors.averagingDouble(Map.Entry::getValue) //(v -> Double.parseDouble(String.format("%.4f", v.getValue())))
                    )) //;
                    // 保留4位小数
            .entrySet().stream().collect(Collectors.toMap(
                            Map.Entry::getKey,
                            v -> Double.parseDouble(String.format("%.4f", v.getValue()))
                    ));


            System.out.println(JSONObject.parseObject(JSONObject.toJSONString(averagedMap)));
            // {"0":0.35,"1":0.45,"2":0.55,"3":0.65}
        }


        /**
         * 对每个 Map<Long, Map<String, Map<Integer, Double>>> 的内层 Map<Integer, Double> 的 Double 值求和，并根据求和结果进行排序，同时处理求和值相同的情况
         *
         * 1.过滤并合并 Map<String, Map<Integer, Double>> 到一个 Map<Integer, Double>。
         * 2.对每个 Map<Integer, Double> 的 Double 值求和。
         * 3.对求和结果进行排序，如果求和值相同，排序为平等序号。
         * 4.将结果转换为 Map<Long, Integer>，其中 Integer 值表示序号。
         */
        public static void test6() {
            // 假设原始数据如下
            Map<Long, Map<String, Map<Integer, Double>>> originalMap = new HashMap<>();

            // 填充示例数据
            Map<String, Map<Integer, Double>> innerMap1 = new HashMap<>();
            innerMap1.put("key1", new HashMap<Integer, Double>(){{
                put(1, 0.3);
                put(2, 0.4);
                put(3, 0.5);
            }});
            innerMap1.put("key2",new HashMap<Integer, Double>(){{
                put(3, 3.3);
                put(4, 4.4);
            }});

            Map<String, Map<Integer, Double>> innerMap2 = new HashMap<>();
            innerMap2.put("key3", new HashMap<Integer, Double>(){{
                put(1, 0.2);
                put(2, 0.5);
                put(3, 0.8);
            }});
            innerMap2.put("key4", new HashMap<Integer, Double>(){{
                put(7, 7.7);
                put(8, 8.8);
            }});

            Map<String, Map<Integer, Double>> innerMap3 = new HashMap<>();
            innerMap3.put("key5", new HashMap<Integer, Double>(){{
                put(1, 0.1);
                put(2, 0.3);
                put(3, 0.8);
            }});

            Map<String, Map<Integer, Double>> innerMap4 = new HashMap<>();
            innerMap4.put("key7", new HashMap<Integer, Double>(){{
                put(1, 0.2);
                put(2, 0.3);
                put(3, 0.6);
            }});

            originalMap.put(1L, innerMap1);
            originalMap.put(2L, innerMap2);
            originalMap.put(3L, innerMap3);
            originalMap.put(4L, innerMap4);

            // 过滤第一个 Map 的 String 键并转换结果
            Map<Long, Double> sumMap = originalMap.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> entry.getValue().entrySet().stream()
                                    .filter(innerEntry -> filterCondition(innerEntry.getKey())) // 替换为你的过滤条件 filterCondition(innerEntry.getKey())
                                    .flatMap(innerEntry -> innerEntry.getValue().entrySet().stream())
                                    .mapToDouble(Map.Entry::getValue)
                                    .sum()
                    ))
                    .entrySet().stream().collect(Collectors.toMap(
                            Map.Entry::getKey,
                            v -> Double.parseDouble(String.format("%.4f", v.getValue()))
                    ))
                    ;
            System.out.println(JSONObject.parseObject(JSONObject.toJSONString(sumMap)));
            // {"1":1.2,"2":1.5,"3":1.2,"4":1.1}

            // 对求和结果进行排序，并处理平等序号
            List<Map.Entry<Long, Double>> sortedEntries = sumMap.entrySet().stream()
                    .sorted(Map.Entry.<Long, Double>comparingByValue(Comparator.reverseOrder()))
                    .collect(Collectors.toList());
            System.out.println(sortedEntries);
            // [2=1.5, 1=1.2, 3=1.2, 4=1.1]

            Map<Long, Integer> result = new LinkedHashMap<>();
            int rank = 1;
            int previousRank = 1;
            Double previousValue = null;

            for (int i = 0; i < sortedEntries.size(); i++) {
                Map.Entry<Long, Double> entry = sortedEntries.get(i);
                if (previousValue != null && !previousValue.equals(entry.getValue())) {
                    rank = i + 1;
                }
                result.put(entry.getKey(), rank);
                previousValue = entry.getValue();
            }

            // 输出结果
            System.out.println(JSONObject.parseObject(JSONObject.toJSONString(result)));
            // {"1":2,"2":1,"3":2,"4":4}
        }
        // 示例过滤条件：仅保留以 "key1" 或 "key3" 开头的键
        private static boolean filterCondition(String key) {
            return key.startsWith("key1") || key.startsWith("key3")|| key.startsWith("key5")|| key.startsWith("key7");
        }
        
        
        private static int test7(double d1, double d2) {
            int i = 0;
            double d = Double.parseDouble(String.format("%.4f", d1 - d2));
            if (d >= 0.01) {
                i = (int) (d * 100);
            }
            System.out.println("i = " + i);
            return i;
        }

        public static void test8() {
            Map<String, Map<Integer, Double>> innerMap1 = new HashMap<>();
            innerMap1.put("g1-c1-e1", new HashMap<Integer, Double>(){{
                put(1, 0.1);
                put(2, 0.2);
            }});
            innerMap1.put("g1-c1-e2",new HashMap<Integer, Double>(){{
                put(1, 0.3);
                put(2, 0.4);
            }});
            innerMap1.put("g1-c2-e1", new HashMap<Integer, Double>(){{
                put(1, 0.5);
                put(2, 0.6);
            }});
            innerMap1.put("g1-c2-e2",new HashMap<Integer, Double>(){{
                put(1, 0.7);
                put(2, 0.8);
            }});

            innerMap1.put("g2-c1-e1", new HashMap<Integer, Double>(){{
                put(1, 0.2);
                put(2, 0.2);
            }});
            innerMap1.put("g2-c1-e2",new HashMap<Integer, Double>(){{
                put(1, 0.4);
                put(2, 0.4);
            }});
            innerMap1.put("g2-c2-e1", new HashMap<Integer, Double>(){{
                put(1, 0.5);
                put(2, 0.6);
            }});
            innerMap1.put("g2-c2-e2",new HashMap<Integer, Double>(){{
                put(1, 0.7);
                put(2, 0.8);
            }});

            // 所有Integer的平均值
            Map<Integer, Double> map = innerMap1.values().stream()
                    .flatMap(innerMap -> innerMap.entrySet().stream())
                    .collect(Collectors.groupingBy(
                            Map.Entry::getKey,
                            Collectors.averagingDouble(Map.Entry::getValue)
                    ))
                    .entrySet().stream().collect(Collectors.toMap(
                            Map.Entry::getKey,
                            v -> Double.parseDouble(String.format("%.4f", v.getValue()))
                    ));
            System.out.println(JSONObject.parseObject(JSONObject.toJSONString(map)));
            // {"1":0.425,"2":0.5}

            // 按条件重新分组后，分组后分组的Integer的平均值
            Map<String, Map<Integer, Double>> map1 = innerMap1.entrySet().stream()
                    .collect(Collectors.groupingBy(
                            m -> m.getKey().split("-")[0],
                            Collectors.collectingAndThen(
                                    Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue),
                                    m -> m.values().stream()
                                            .flatMap(innerMap -> innerMap.entrySet().stream())
                                            .collect(Collectors.groupingBy(
                                                    Map.Entry::getKey,
                                                    Collectors.averagingDouble(Map.Entry::getValue)
                                            )).entrySet().stream().collect(Collectors.toMap(
                                                    Map.Entry::getKey,
                                                    v -> Double.parseDouble(String.format("%.4f", v.getValue()))
                                            ))
                            )
                    ));
            System.out.println(JSONObject.parseObject(JSONObject.toJSONString(map1)));
            // {"g1":{"1":0.4,"2":0.5},"g2":{"1":0.45,"2":0.5}}

        }


    }


}
