package com.lp.demo.action.leetcode_in_action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * @create 2020/11/10 12:02
 * @auther outman
 **/
public class OtherDemo {
    public static void main(String[] args) {
        //整数分解成两个质数和
        numSplit();

        //数据分类
        dataClassify();
    }

    /**
     * 如果一个整数可以分解成两个质数的和，求有这样的质数的组数，比如18可以分解成7+11、5+13，即两组。
     * 支持一次输入多行，以0结束输入，输出以"end"字符串结束，下面是样例
     *      输入：
     *              2
     *              5
     *              10
     *              18
     *              0
     *      输出：
     *              0
     *              1
     *              2
     *              2
     *              end
     */
    public static void numSplit() {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            int num = sc.nextInt();
            List<Integer> nums = new ArrayList<>();
            while (num != 0) {
                nums.add(num);
                num = sc.nextInt();
            }
            sc.nextLine();
            goldBach(nums);
        }
        sc.close();
    }
    public static void goldBach(List<Integer> nums){
        for (int n : nums) {
            int count = 0;
            if (n > 2) {
                for (int i = 2; i <= n/2; i++) {
                    if (ifPrime(i) && ifPrime(n-i)) {
                        count++;
//                        System.out.println("数为: " +i + " 和" + (n-i) );
                    }
                }
                System.out.println(count > 0 ? count : 0);
            } else {
                System.out.println(0);
            }
        }
        System.out.println("end");
    }
    //判断数是否是质数
    public static boolean ifPrime(int num){
        for (int i = 2; i <num; i++) {
            if (num % i==0) {
                return false;
            }
        }
        return true;
    }


    /**
     * 对一个数据a进行分类，分类方法为：此数据a（四个字节大小）的四个字节相加 % 一个给定的值，如果得到的结果小于一个给定的值c，则此结果即为数据a的类型；如果得到的结果大于或者等于c，则此结果无效即为数据a的类型无效。
     * 比如一个数据a = 0x01010101，b = 3，按照分类方法计算（0x01 + 0x01 + 0x01 + 0x01）% 3 = 1。所以如果 c = 2，则此a的类型是1，如果c = 1，则此a的类型是无效。
     * 计算数据最多的类型（有效类型）有多少个数据??
     * <p>
     * 输入：3 4 256 257 258 259 260 261 262 263 264 265
     * 输出：3
     * 思路：
     *      a、给定了一个数字a，需要先将数字a转换成十六进制来表示；
     *      b、将十六进制按照两个一组进行叠加；
     *      c、将叠加以后的数字与b求余；
     *      d、将求余以后的结果与c进行比较，如果大于等于c则是一个无效的数字，如果小于c则有效。
     *      e、针对题目中给定的数组a，需要对每一个a中的数字进行步骤a~d的一个计算，分类出求余的结果有多少中，然后统计每一种结果一共有多少个，最后输出最多的那个的数量。
     * *出错点*：
     *      %d 十进制（整数类型）
     *      %x 十六进制（整数类型） ("%08x", 256) = 00000100
     *      %o 八进制（整数类型）
     */
    public static void dataClassify() {
        Scanner in = new Scanner(System.in);
        String nums = in.nextLine();
        String[] strs = nums.split(" ");
        int c = Integer.valueOf(strs[0]);
        int b = Integer.valueOf(strs[1]);

        String[] datas = Arrays.copyOfRange(strs, 2, strs.length);
        Map<String, Integer> countMap = new HashMap<>();
        for (int i = 0; i < datas.length; i++) {
            System.out.println(Integer.parseInt(datas[i]));
            String str1 = Integer.toBinaryString(Integer.parseInt(datas[i]));
            String str2 = String.format("%08x", Integer.valueOf(str1));
            String[] strings = splitStr(str2);

            int a = 0;
            for (int j = 0; j < strings.length; j++) {
                a += Integer.parseInt(strings[j], 16);
            }

            int num = a % b;
            if (num < c) {
                Integer temp = countMap.get(String.valueOf(num));
                if (temp == null) {
                    temp = 1;
                } else {
                    temp += 1;
                }
                countMap.put(String.valueOf(num), temp);
            }
        }
        int maxNum = 0;
        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() > maxNum) {
                maxNum = entry.getValue();
            }
        }
        System.out.println(maxNum);

    }

    public static String[] splitStr(String str) {
        String[] bytes = new String[4];
        int n = 0;
        while (str.length() >= 2) {
            bytes[n] = str.substring(0, 2);
            str = str.substring(2);
            n++;
        }
        return bytes;
    }
}
