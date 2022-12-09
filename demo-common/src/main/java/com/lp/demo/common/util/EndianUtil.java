package com.lp.demo.common.util;

import cn.hutool.core.util.StrUtil;

/**
 * @author lp
 * @date 2022/2/25 11:25
 **/
public class EndianUtil {
    public static void main(String[] args) {
        String bigEndian = "620927f462098e6000fc00580022003d000d" +
                "620927f404180200" +
                "62092c0c051f0100" +
                "6209312b04180400" +
                "6209354303120300" +
                "62093855072b0100" +
                "62093f8006250100" +
                "620945a5051f0100" +
                "62094ac4051f0400" +
                "62094fe3051f0300" +
                "62095502051f0400" +
                "62095a210a730100" +
                "6209649410b80200" +
                "6209754c19140100";

        String value = "FFFEFDFCFBFAF9080706050403020100"; // 如果是奇数个字符会忽略掉最后一位
        for (int i = 0; i < value.length() / 2; i++) {
            String convertEndian = convertEndian(value.substring(i == 0 ? 0 : 2 * i));
            System.out.println("convertEndian = " + convertEndian);
        }
    }

    /**
     * 二进制字符串大小端模式转换
     *
     * @param data
     * @return
     */
    public static String convertEndian(String data) {
        // 如果数据为空，则直接返回
        if (data == null || data.isEmpty()) {
            return data;
        }

        // 去除首尾空字符
        data = data.trim();

        // 空字符校验
        if (StrUtil.containsBlank(data)) {
            throw new IllegalArgumentException(data);
        }

        // 将字符串转换为字节数组
        byte[] bytes = new byte[data.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            int high = Character.digit(data.charAt(i * 2), 16);
            int low = Character.digit(data.charAt(i * 2 + 1), 16);
            bytes[i] = (byte) ((high << 4) | low);
        }

        // 调用字节数组版本的方法进行转换
        byte[] result = convertEndian(bytes);

        // 将字节数组转换为字符串并返回
        StringBuilder sb = new StringBuilder();
        for (byte b : result) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * 字节数组大小端模式转换
     *
     * @param data 字节数组
     * @return
     */
    public static byte[] convertEndian(byte[] data) {
        // 如果数组为空，则直接返回
        if (data == null || data.length == 0) {
            return data;
        }

        // 创建结果数组
        byte[] result = new byte[data.length];

        // 遍历数组，将每个字节的顺序翻转
        for (int i = 0; i < data.length; i++) {
            result[i] = data[data.length - i - 1];
        }

        // 返回结果
        return result;
    }
}
