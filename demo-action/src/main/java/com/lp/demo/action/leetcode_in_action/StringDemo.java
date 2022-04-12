package com.lp.demo.action.leetcode_in_action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * @create 2020/11/28 19:42
 * @auther outman
 **/
public class StringDemo {
    public static void main(String[] args) {
        //替换空格
        String s = "We are happy.";
        System.out.println(replaceBlack(s));

        // Z字型变换
        String str = "ABCDEFGHIJK";
        System.out.println(convert(str, 3));

        // 判断是否是子序列
        System.out.println(isSubsequence2("abc", "ahbgdc"));  //true
        System.out.println(isSubsequence2("axc", "ahbgdc"));  //false

        //字符串的排列组合
        String[] strs = permutation("abc");
        System.out.println(Arrays.toString(strs));

    }

    /**
     * 替换空格
     * 描述：实现一个函数，把字符串中的每个空格替换成"%20"。
     * 示例：
     *      输入："We are happy."
     *      输出："We%20are%20happy."
     * 注意：
     *      陷阱：如果直接使用replace：s.replace(" ", "%20"); 原来一个" "空格字符，替换后变成'%'、'2'、'0'三个字符。在原字符串上替换，可能会覆盖修改该字符串后面的内存，需要确定内存是否足够。（在新字符串上替换，我们可以自己分配足够内存）
     * 思路：
     *      从后往前复制，数组长度会增加
     *      使用StringBuilder、StringBuffer类; 时间复杂度：O(n);空间复杂度：O(n);
     */
    public static String replaceBlack(String s) {
        if (s == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (String.valueOf(s.charAt(i)).equals(" ")) {
                sb.append("%20");
            } else {
                sb.append(s.charAt(i));
            }
        }
        return sb.toString();
    }

    /**
     * 6. Z字形变换
     * 描述：将一个给定字符串根据给定的行数，以从上往下、从左到右进行 Z 字形排列。
     *      比如输入字符串为 "LEETCODEISHIRING" 行数为 3 时，排列如下：
     *          L   C   I   R
     *          E T O E S I I G
     *          E   D   H   N
     *      之后，你的输出需要从左往右逐行读取，产生出一个新的字符串，比如："LCIRETOESIIGEDHN"。
     *      请你实现这个将字符串进行指定行数变换的函数：string convert(string s, int numRows);
     * 示例：
     *      输入: s = "LEETCODEISHIRING", numRows = 3
     *      输出: "LCIRETOESIIGEDHN"
     * 示例2：
     *      输入: s = "LEETCODEISHIRING", numRows = 4
     *      输出: "LDREOEIIECIHNTSG"
     *      解释:
     *          L     D     R
     *          E   O E   I I
     *          E C   I H   N
     *          T     S     G
     * 思路：
     *      模拟使用行索引，遍历s时，将每个字符填到对应的行res[i]
     *      res[i] += c;把每个字符c加入对应行si
     *      i += flag；更新当前字符c对应的行索引
     *      flag = -flag；在达到Z字形转折点时，执行反向。
     * 时间复杂度：O(n);
     * 空间复杂度：O(n);
     */
    public static String convert(String s, int numRows) {
        if (numRows < 2) {
            return s;
        }
        List<StringBuilder> rows = new ArrayList<>();
        for (int i = 0; i < numRows; i++) {
            rows.add(new StringBuilder());
        }
        int i = 0;
        int flag = -1;
        for (char c : s.toCharArray()) {
            rows.get(i).append(c);
            if (i == 0 || i == numRows - 1) {
                flag = -flag;
            }
            i += flag;
        }
        StringBuilder res = new StringBuilder();
        for (StringBuilder row : rows) {
            res.append(row);
        }
        System.out.println(rows);
        return res.toString();
    }

    /**
     * 392. 判断子序列
     * 描述：给定字符串 s 和 t ，判断 s 是否为 t 的子序列。
     *      字符串的一个子序列是原始字符串删除一些（也可以不删除）字符而不改变剩余字符相对位置形成的新字符串。（例如，"ace"是"abcde"的一个子序列，而"aec"不是）。
     * 示例：
     *      输入：s = "abc", t = "ahbgdc"
     *      输出：true
     *
     *      输入：s = "axc", t = "ahbgdc"
     *      输出：false
     * 思路：
     *      双指针(贪心算法)：初始化两个指针 i和j，分别指向 s和t 的初始位置。每次贪心地匹配，匹配成功则i和j同时右移，匹配 s的下一个位置，匹配失败则 j 右移，i不变，尝试用 t的下一个字符匹配 s。
     * 时间复杂度：O(m+n)；空间复杂度：O(1)
     */
    public static boolean isSubsequence(String s, String t) {
        if (s == null || t == null) {
            return false;
        }

        int sLen = s.length();
        int tLen = t.length();
        if (sLen > tLen) {
            return false;
        }
        int i = 0, j = 0;
        while (i < sLen && j < tLen) {
            if (s.charAt(i) == t.charAt(j)) {
                i++;
            }
            j++;
        }
        return i == sLen;
    }
    // 其他方式
    public static boolean isSubsequence2(String s, String t) {
        if (s == null || t == null) {
            return false;
        }
        int sLen = s.length();
        int tLen = t.length();
        if (sLen > tLen) {
            return false;
        }
        char[] chars = s.toCharArray();
        int i = 0;
        boolean flag = false;
        StringBuilder sb = new StringBuilder(t);
        while (!flag) {
            if (sb.toString().contains(String.valueOf(chars[i]))) {
                sb.delete(0, sb.indexOf(String.valueOf(chars[i]))+1);
                ++i;
            } else {
                break;
            }
            if (i == sLen) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 题目：字符串的排列
     * 描述：输入一个字符串，打印出该字符串中字符的所有排列。
     *      你可以以任意顺序返回这个字符串数组，但里面不能有重复元素。
     * 输入：s = "abc"
     * 输出：["abc","acb","bac","bca","cab","cba"]
     * 回溯算法
     */
    public static List<String> res = new LinkedList<>();
    public static char[] c;
    public static String[] permutation(String s) {
        c = s.toCharArray();
        dfs(0);
        return res.toArray(new String[res.size()]);
    }
    public static void dfs(int x) {
        if(x == c.length - 1) {
            res.add(String.valueOf(c)); // 添加排列方案
            return;
        }
        HashSet<Character> set = new HashSet<>();
        for(int i = x; i < c.length; i++) {
            if(set.contains(c[i])) {
                continue; // 重复，因此剪枝
            }
            set.add(c[i]);
            swap(i, x); // 交换，将 c[i] 固定在第 x 位
            dfs(x + 1); // 开启固定第 x + 1 位字符
            swap(i, x); // 恢复交换
        }
    }
    public static void swap(int a, int b) {
        char tmp = c[a];
        c[a] = c[b];
        c[b] = tmp;
    }
}



