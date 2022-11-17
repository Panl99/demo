package com.lp.demo.common.util;

import com.lp.demo.common.exception.DisplayableException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Stack;

import static jdk.nashorn.internal.objects.NativeString.substring;

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
//        int heartRateLittleEndianTime = 0x28083b62;
//        int heartRateLittleEndianData = 0x3c000000;
//        int heartRateBigEndianTime = 0x623b0828;
//        int heartRateBigEndianData = 0x0000003c;
        String heartRateLittleEndianTime = "28083b62";
        String heartRateLittleEndianData = "3c000000";
        String heartRateBigEndianTime = "623b0828";
        String heartRateBigEndianData = "0000003c";

/*        StringBuilder big2Little = new StringBuilder();
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

        System.out.println(">>>>>>>>>>" + Integer.reverseBytes(Integer.parseInt(heartRateLittleEndianTime, 16)));*/

        String oneBytes = "62";
        String twoBytes = "9c18";//"61a6";
        String threeBytes = "623b08";
        String fourBytes = "fefdfaf9";//"61a64cd8";
        String fiveBytes = "623b082801";

        System.out.println("convertEndian(twoBytes, 2) = " + convertEndian(twoBytes, 2));
        System.out.println("convertEndian(fourBytes, 4) = " + convertEndian(fourBytes, 4));
        System.out.println("convertEndian(threeBytes, 3) = " + convertEndian(threeBytes, 3));


        System.out.println("0xd8 | 0xFF = " + (0xd8 & 0xFF));
        System.out.println("-40 | 0xFF = " + (-40 & 0xFF));
        System.out.println("(0xd8 ^ 0xFF) = " + (0xd8 ^ 0xFF));
        System.out.println("(0xd8 >> 8) = " + (0xd8 >> 8));

        int i = Integer.parseInt("ffff", 16);
        System.out.println("i = " + i);
        System.out.println("(short) i = " + ((short) i & 0xFFFF));

        convertEndian();

        String value = "7FFEFDFCFBFAF908";
        String s = convertEndianByByteBuf(value);
        System.out.println("s = " + s);
        long l = Long.parseLong(s, 16);
        System.out.println("l = " + l);
    }

    /**
     * 大小端转换
     *
     * @param value    大端/小端数据（16进制最大4字节）
     * @param capacity 字节数组容量（取决于数据字节数）
     * @return
     */
    public static String convertEndian(String value, int capacity) {
        System.out.println("value = " + value);
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[capacity]);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        if (capacity % 4 == 0) {
            byteBuffer.asIntBuffer().put((int) Long.parseLong(value, 16));
        } else if (capacity % 2 == 0) {
            byteBuffer.asShortBuffer().put((short) Integer.parseInt(value, 16));
        }

        StringBuilder s = new StringBuilder();
        for (byte b : byteBuffer.array()) {
            String hexString = Integer.toHexString(b & 0xFF);
            if (hexString.length() < 2) {
                hexString = "0" + hexString;
            }
            s.append(hexString);
        }
//        System.out.println("big -> little = " + s);
        return s.toString();
    }


    /**
     * 大小端转换，手动转，不限长度
     */
    public static void convertEndian() {
        String value = "0102030405060708090A0B0C0D0E0F";
        Stack<String> stack = new Stack<>();
        String byteValue = "";
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i != 0 && i % 2 == 0) {
                stack.push(byteValue);
                byteValue = "";
            }
            byteValue += chars[i];
        }
        if (byteValue.length() == 1) {
            byteValue = "0" + byteValue;
        }
        stack.push(byteValue);
        if (stack.isEmpty()) {
            return;
        }
        String pop = "";
        while (!stack.isEmpty()) {
            pop += stack.pop();
        }
        System.out.println("pop = " + pop);
    }

    /**
     *
     * @param value  最大Long.MAX_VALUE
     * @return
     */
    public static String convertEndianByByteBuf(String value) {
        if (StringUtil.isEmpty(value)) {
            return "";
        }
        if (value.length() % 2 != 0) {
            return "";
        }
        if (value.length() > 16) {
            return "";
        }
        if (value.length() == 16 && Integer.parseInt(value.substring(0, 2), 16) > 0x7F) {
            return "";
        }
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        byteBuf.writeLongLE(Long.parseLong(value, 16));
        long longBE = byteBuf.getLong(0);
        System.out.println("longBE = " + longBE);
        String hexBE = Long.toHexString(longBE);
        if (hexBE.length() % 2 != 0) {
            hexBE = "0" + hexBE;
        }
        return hexBE.substring(0, value.length());
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

    private byte[] shortToByteArray(short value, boolean bigEndian) {
        byte[] bytes;
        byte b1 = (byte) ((value >> 24) & 0xFF);
        byte b0 = (byte) (value & 0xFF);
        if (bigEndian) {
            bytes = new byte[]{b1, b0};
        } else {
            bytes = new byte[]{b0, b1};
        }

        return bytes;
    }

    private byte[] intToByteArray(int value, boolean bigEndian) {
        byte[] bytes;
        byte b3 = (byte) ((value >> 24) & 0xFF);
        byte b2 = (byte) ((value >> 16) & 0xFF);
        byte b1 = (byte) ((value >> 8) & 0xFF);
        byte b0 = (byte) (value & 0xFF);

        if (bigEndian) {
            bytes = new byte[]{b3, b2, b1, b0};
        } else {
            bytes = new byte[]{b0, b1, b2, b3};
        }
        return bytes;
    }
}
