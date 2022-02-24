package com.lp.demo.common.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lp.demo.common.aop.thread.ThreadPoolUtil;
import com.lp.demo.common.dto.UserDto;
import com.lp.demo.common.enums.DeviceSceneTypeBaiduEnum;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author lp
 * @date 2021/5/27 20:42
 * @description
 **/
public class StringUtil {

    /**
     * 字符串是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * 字符串是否是空字符串
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        if (isEmpty(str)) {
            return true;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 校验多个字符串是否为空
     * @param strs 字符串数组
     * @return 全部为空返回true，否则false
     */
    public static boolean isAllEmpty(String... strs) {
        for (String s : strs) {
            if (!isEmpty(s)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 校验多个字符串是否为空
     * @param strs 字符串数组
     * @return 至少一个为空返回true，否则false
     */
    public static boolean isAnyEmpty(String... strs) {
        for (String s : strs) {
            if (isEmpty(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 校验多个字符串是否为空或者"null"
     * @param strs
     * @return 至少一个为空或为字符串"null"返回true，否则false
     */
    public static boolean isAnyNullOrEmpty(String... strs) {
        for (String s : strs) {
            if (isNullOrEmpty(s)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 字符串是否为空或者"null"
     * @param s
     * @return
     */
    public static boolean isNullOrEmpty(String s) {
        if (isEmpty(s) || "null".equalsIgnoreCase(s)) {
            return true;
        }
        return false;
    }

    /**
     * 校验多个字符串是否为空或空字符
     * @param strs 字符串数组
     * @return 全部为空返回true，否则false
     */
    public static boolean isAllBlank(String... strs) {
        for (String s : strs) {
            if (!isBlank(s)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 校验多个字符串是否为空或空字符
     * @param strs 字符串数组
     * @return 至少一个为空返回true，否则false
     */
    public static boolean isAnyBlank(String... strs) {
        for (String s : strs) {
            if (isBlank(s)) {
                return true;
            }
        }
        return false;
    }


    public static void sleep(int i) {
        try {
            TimeUnit.SECONDS.sleep(i);
            ConsoleColorUtil.printDefaultColor("sleep "+i+"s ");
        } catch (InterruptedException e) {
        }
    }

    public static void main(String[] args) throws Exception {
       /* ConsoleColorUtil.printDefaultColor("start...");
//        testThenApply();
//        testSupplyAsync();
//        testRunAsync();
        testWhenComplete();
        ConsoleColorUtil.printDefaultColor("start sleep...");
        sleep(1);
//        testThenAccept();
//        testThenRun();
        ConsoleColorUtil.printDefaultColor("end...");
        testWhenComplete();
        sleep(3);
        testWhenComplete();

        List<Integer> list = new ArrayList();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        list.forEach(i -> {
            if (i % 7 == 0) {
                return;
            }
            System.out.println("i = " + i);
        });

        String sleep = DeviceSceneTypeBaiduEnum.contains(null);
        System.out.println("sleep = " + sleep);
*/

/*        int max = decodeHEX("d2ff");
        System.out.println("max = " + max);
        int i = decodeHEX("d201");
        System.out.println("i = " + i);
        String s = toHexString2Bytes(i);
        System.out.println("s = " + s);

        for (int j = 1; j <= 20; j++) {
            System.out.println("int2Bytes(++i) = " + toHexString2Bytes(++i));
        }*/
/*
        Long l = 105600L;
        double l1 = l / 1000.00;
        System.out.println("l1 = " + l1);
        Double d = Double.parseDouble(l1+"");
        System.out.println("d = " + d);
        String format = String.format("%.2f", d);
        System.out.println("format = " + format);
        Double df = Double.parseDouble(format);
        System.out.println("df = " + df);*/

/*        List<Integer> list = new ArrayList<>();
//        list.add(1);
//        list.add(2);
//        list.add(5);
//        list.add(8);
//        list.add(6);
//        list.add(3);
        for (int i = 0; i < 1000000; i++) {
            list.add(i);
        }


        int i = ThreadLocalRandom.current().nextInt(0, 10);
        System.out.println("i = " + i);
        switch (i % 2) {
            case 0:
                Collections.reverse(list);
                System.out.println("list = " + list);
                break;
            case 1:
                System.out.println("list = " + list);
        }
        */

/*        String s = covertSecondToTime(600020);
        System.out.println("s = " + s);

        System.out.println(166 * 60 * 60 + 40 * 60);*/

/*        UserDto userDto = UserDto.initUserDto();
        UserDto userDto2 = UserDto.initUserDto();
        JSONArray jsonObject = new JSONArray();
        jsonObject.add(userDto);
        jsonObject.add(userDto2);
        System.out.println("userDto = " + jsonObject);

        List<UserDto> userDtos = JSONArray.parseArray(jsonObject.toJSONString(), UserDto.class);
        System.out.println("userDto1 = " + userDtos);*/

/*        double i = 17.00 * 1000;
        System.out.println("i = " + i);
        double v1 = i / (60 * 18);
        System.out.println("v1 = " + v1);
        double v = v1 * 3.6;
        System.out.println("v  = " + v);
        String format = String.format("%.2f", v1 * 3.6);
        System.out.println("format = " + format);

        int avgPace =  (int) (60 * 18 / 17.00);
        System.out.println("avgPace = " + (avgPace < 60 ? "00'" + avgPace + "''" : avgPace / 60 + "'" + avgPace % 60 + "''"));


        long steps = 9000;
        long time = 3600;
        int bupin = (int) ((double) steps / time * 60);
        System.out.println("bupin = " + bupin);

        double dis = 8.50;
        int bufu = (int) (dis * 1000 * 100 / steps);
        System.out.println("bufu = " + bufu);

        double plusDouble = Double.parseDouble(format) + dis;
        System.out.println("plusDouble = " + plusDouble);*/

/*        double d = 19352.52000000076;
        DecimalFormat df = new DecimalFormat("#.00");
        String formatDouble = df.format(d);
        System.out.println("formatDouble = " + formatDouble);

        Double aDouble = Double.valueOf(formatDouble);
        System.out.println("aDouble = " + aDouble);*/

/*        Date now = new Date();
        String month = new SimpleDateFormat("yyyy-MM").format(now);
        System.out.println("month = " + month);
        Date firstDayOfMonth = DateTimeUtil.getFirstDayOfMonth(now);
        System.out.println("firstDayOfMonth = " + firstDayOfMonth);*/

/*
        for (int i = 0; i < 10; i++) {
            int nextInt = ThreadLocalRandom.current().nextInt(1, 3);
            System.out.println("nextInt = " + nextInt);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        String format = sdf.format(new Date());
        System.out.println("format = " + format);
        System.out.println("new Date().getDay() = " + new Date().getHours());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTime = dateFormat.parse("2022-03-01 00:00:00");
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(startTime);
        int month = c.get(Calendar.WEEK_OF_MONTH);
        System.out.println("month = " + month);
*/


        String path = "https://osslocal.com/dial/image/tx.png";
        String s = path.substring(path.lastIndexOf(".") + 1);
        System.out.println("s = " + s);
    }
    /**
     * 秒转换成 时:分:秒
     *
     * @param time
     * @return
     */
    private static String covertSecondToTime(long time) {
        if (time < 60) {
            return "00:00:" + formatTime(time);
        }
        if (time < 60 * 60) {
            return "00:" + formatTime(time / 60) + ":" + formatTime(time % 60);
        } else {
            String hour = formatTime(time / 3600);
            String minute = formatTime(time % 3600 / 60);
            String second = formatTime(time % (60 * 60 * 60) % 60);
            return hour + ":" + minute + ":" + second;
        }
    }
    private static String formatTime(long time) {
        if (time < 10) {
            return "0" + time;
        } else {
            return String.valueOf(time);
        }
    }

    public static String toHexString2Bytes(int intValue){
        Integer value = intValue;
        String hex = Integer.toHexString(value);
        if(hex.length() == 1){
            hex = "0" + hex + "00";
        }
        if(hex.length() == 2){
            hex = hex + "00";
        }
        if(hex.length() == 3){
            hex = "0" +  hex;
        }
        return hex;
    }

    public static int decodeHEX(String hex){
        BigInteger bigint=new BigInteger(hex, 16);
        int numb=bigint.intValue();
        return numb;
    }


    /**
     * testThenApply
     * thenApply 当前阶段执行完后，结果作为下一阶段的参数。
     */
    public static void testThenApply() throws Exception {
        CompletableFuture future = CompletableFuture.supplyAsync(() -> 1)
                .thenApply(i -> ++i)
                .thenApply(i -> i << 2)
                .whenComplete((r,e) -> System.out.println("testThenApply..." + r))
                .exceptionally((e) -> {
                    e.printStackTrace();
                    return null;
                });
        future.get();
    }

    /**
     * supplyAsync 有返回值,没有指定Executor的方法会使用ForkJoinPool.commonPool() 作为它的线程池执行异步代码
     * runAsync 没有返回值
     */
    public static void testSupplyAsync() throws Exception{
        CompletableFuture future = CompletableFuture.supplyAsync(() -> 100 >> 2)
                .thenApplyAsync(i -> i * 10);
        System.out.println("testSupplyAsync..." + future.get());
    }
    public static void testRunAsync() throws Exception {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
            }
            System.out.println("testRunAsync end ...");
        });
        System.out.println("testRunAsync..." + future.get());
    }

    /**
     * testWhenComplete
     * whenComplete 当前任务线程执行后，如果该线程执行慢没有立即返回结果时，使用whenComplete等待获取处理执行完后的结果，不会像使用get()一样阻塞住线程
     */
    public static void testWhenComplete() throws Exception {
        CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
            }
            if(new Random().nextInt() % 2 >= 0) {
                int i = 12 / 5;
            }
            ConsoleColorUtil.printDefaultColor("testWhenComplete...");
        }, ThreadPoolUtil.getExecutor())
                .whenComplete((r,e) -> {
                    ConsoleColorUtil.printDefaultColor("testWhenComplete执行完成！res=" + r);
//                    ThreadPoolUtil.stop();
                })
                .exceptionally(e -> {
                    ConsoleColorUtil.printDefaultColor("testWhenComplete执行失败！" + e.getMessage());
                    ThreadPoolUtil.stop();
                    return null;
                });

//        TimeUnit.SECONDS.sleep(2);
    }

    /**
     * testThenAccept
     * thenAccept 接收任务结果（上个任务结果会传过来），并消费，无返回值
     */
    public static void testThenAccept() throws Exception {
        CompletableFuture.supplyAsync(() -> "testThenAccept...")
                .thenAccept(System.out::println);
    }

    /**
     * testThenRun
     * thenRun 不接受任务结果（上个任务结果不会传过来），只是在上个任务执行完成后执行thenRun方法
     */
    public static void testThenRun() throws Exception {
        CompletableFuture.supplyAsync(() -> 100)
                .thenRun(() -> System.out.println("testThenRun..."));
    }


}
