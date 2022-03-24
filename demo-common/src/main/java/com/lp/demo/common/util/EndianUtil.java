package com.lp.demo.common.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

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
/*
        int x = 0x623b0828;
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[4]);
        byteBuffer.asIntBuffer().put(x);
        String before = Arrays.toString(byteBuffer.array());
        System.out.println("默认字节序：" + byteBuffer.order().toString() + "," + "内存数据：" + before);

        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.asIntBuffer().put(x);
        String after = Arrays.toString(byteBuffer.array());
        System.out.println("小端字节序：" + byteBuffer.order().toString() + "," + "内存数据：" + after);
*/


        String heartRateLittleEndian = "28083b623c000000";
        int heartRateLittleEndianTime = 0x28083b62;
        int heartRateLittleEndianData = 0x3c000000;
        int heartRateBigEndianTime = 0x623b0828;
        int heartRateBigEndianData = 0x0000003c;

        StringBuilder big2Little = new StringBuilder();
        big2Little.append(convertEndian(heartRateBigEndianTime, 4));
        big2Little.append(convertEndian(heartRateBigEndianData, 4));
        System.out.println("big -> little result = " + big2Little);

        StringBuilder little2Big = new StringBuilder();
//        for (int i = 0; i < heartRateLittleEndian.length() / 8; i++) {
//            s.append(littleEndianToBigEndian(Integer.parseInt(heartRateLittleEndian.substring(i * 8, (i + 1) * 8), 16)));
//        }
        little2Big.append(convertEndian(heartRateLittleEndianTime, 4));
        little2Big.append(convertEndian(heartRateLittleEndianData, 4));
        System.out.println("little -> big result = " + little2Big);

        System.out.println(">>>>>>>>>>" + Integer.reverseBytes(heartRateLittleEndianTime));
    }

    /**
     * 大小端转换
     *
     * @param value    大端/小端数据（16进制最大4字节int型数据，如：0x623b0828）
     * @param capacity 字节数组容量（取决于数据字节数）
     * @return
     */
    public static String convertEndian(int value, int capacity) {
        System.out.println("value = " + value);
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[capacity]);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.asIntBuffer().put(value);
        StringBuilder s = new StringBuilder();
        for (byte b : byteBuffer.array()) {
            String hexString = Integer.toHexString(b);
            if (hexString.length() < 2) {
                hexString = "0" + hexString;
            }
            s.append(hexString);
        }
//        System.out.println("big -> little = " + s);
        return s.toString();
    }


/*    public static String convertToLittleEndian(int bigEndian, int capacity) {
        System.out.println("bigEndian = " + bigEndian);
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[capacity]);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.asIntBuffer().put(bigEndian);
        StringBuilder s = new StringBuilder();
        for (byte b : byteBuffer.array()) {
            String hexString = Integer.toHexString(b);
            if (hexString.length() < 2) {
                hexString = "0" + hexString;
            }
            s.append(hexString);
        }
//        System.out.println("big -> little = " + s);
        return s.toString();
    }

    public static String convertToBigEndian(int littleEndian, int capacity) {
        System.out.println("littleEndian = " + littleEndian);
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[capacity]);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byteBuffer.asIntBuffer().put(littleEndian);
        StringBuilder s = new StringBuilder();
        for (byte b : byteBuffer.array()) {
            String hexString = Integer.toHexString(b);
            if (hexString.length() < 2) {
                hexString = "0" + hexString;
            }
            s.append(hexString);
        }
//        System.out.println("little -> big = " + s);
        return s.toString();
    }*/
}
