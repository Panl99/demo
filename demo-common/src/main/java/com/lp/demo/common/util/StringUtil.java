package com.lp.demo.common.util;

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

}
