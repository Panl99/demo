package com.lp.demo.common.util;

import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author lp
 * @date 2025/2/11 14:30
 * @desc
 **/
@Component
public class IdUtil {

    private static final String SERIAL_ID_REGEX = "\\(([A-Za-z]{2}\\d{4}[0-9a-zA-Z]{4})\\)$";
    private static final String ATOMIC_ID_REGEX = "\\(([0-9a-zA-Z]{4})\\)$";
    private static final int LENGTH_LIMIT = 64;

    private static RedissonClient redissonClient;

    @Autowired
    public void setRedissonClient(RedissonClient redissonClient) {
        IdUtil.redissonClient = redissonClient;
    }

    /**
     * 序列id
     *
     * @param type   系统类型
     * @param prefix 系统前缀
     * @return 2位系统标识 + 4位年月时间 + 4位36进制自增序号
     */
    public static String serialId(String type, String prefix) {
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMM"));
        RAtomicLong atomicLong = redissonClient.getAtomicLong("SERIAL_ID:" + type + ":" + dateStr);
        // 设置月底过期
        if (atomicLong.get() == 0) {
            atomicLong.set(0);
            atomicLong.expireAt(DateTimeUtil.getLastDayOfMonth(new Date()).getTime());
        }
        // 4位36进制自增序号
        String atomicIdStr = Long.toString(atomicLong.incrementAndGet(), Character.MAX_RADIX).toUpperCase();
        if (atomicIdStr.length() < 4) {
            atomicIdStr = String.format("%4s", atomicIdStr).replace(' ', '0');
        }
        return prefix + dateStr + atomicIdStr;
    }

    /**
     * 下一个序列id
     *
     * @param type 系统类型
     * @param s    当前字符串
     * @return
     */
    public static String nextSerialId(String type, String prefix, String s) {
        Pattern pattern = Pattern.compile(SERIAL_ID_REGEX);
        Matcher matcher = pattern.matcher(s);

        if (matcher.find()) {
            String item = matcher.group(1);
            // 只自增最后一个
            int lastIndex = s.lastIndexOf(item);
            if (lastIndex != -1) {
                return s.substring(0, lastIndex) + serialId(type, prefix/*item.substring(1, 3)*/) + s.substring(lastIndex + item.length());
            }
        }
        return buildSerialId(type, prefix, s);
    }

    private static String buildSerialId(String type, String prefix, String s) {
        String serialId = "(" + serialId(type, prefix) + ")";
        String result = s + serialId;
        // 限制长度64
        if (result.length() > LENGTH_LIMIT) {
            result = s.substring(0, LENGTH_LIMIT - serialId.length()) + serialId;
        }
        return result;
    }

    /**
     * 匹配并自增序列id
     *
     * @param input
     * @return
     */
    public static String incrementSerialId(String input) {
        Pattern pattern = Pattern.compile(SERIAL_ID_REGEX);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            String suffix = matcher.group(1);
            // 提取 36 进制部分
            String hexPart = suffix.substring(6);

            // 将 36 进制数字转换为十进制
            long decimalValue = Long.parseLong(hexPart, 36);

            // 自增操作
            decimalValue++;

            // 将十进制数字转换回 36 进制
            String incrementedHex = Long.toString(decimalValue, 36).toUpperCase();

            // 保证自增后的数字是 4 位
            while (incrementedHex.length() < 4) {
                incrementedHex = "0" + incrementedHex;
            }

            // 生成新的字符串，替换自增后的 36 进制数字
            String newSuffix = suffix.substring(0, 6) + incrementedHex;
            return input.substring(0, input.length() - suffix.length() - 2) + "(" + newSuffix + ")";
        } else {
            return input;
        }
    }

    public static void main(String[] args) {
        String serialId = incrementSerialId("test(CP25020001)(CP2502001a)(CP250200cc)");
        System.out.println("serialId = " + serialId);
    }

    static class HexGenerator {

        /**
         * 方法1：使用SecureRandom生成8字节随机十六进制数
         * 安全性更高，适合密码学场景
         */
        public static String generateSecureHex8Bytes() {
            SecureRandom secureRandom = new SecureRandom();
            byte[] bytes = new byte[8];
            secureRandom.nextBytes(bytes);

            // 将字节数组转换为十六进制字符串，不足8位补0
            return bytesToHex(bytes);
        }

        /**
         * 方法2：使用Random生成8字节随机十六进制数
         * 性能更好，适合普通随机场景
         */
        public static String generateRandomHex8Bytes() {
            Random random = new Random();
            byte[] bytes = new byte[8];
            random.nextBytes(bytes);

            return bytesToHex(bytes);
        }

        /**
         * 方法3：使用时间戳+随机数生成（自定义算法）
         * 结合时间戳和随机数，减少重复概率
         */
        public static String generateTimestampHex8Bytes() {
            long timestamp = System.currentTimeMillis();
            long randomValue = new Random().nextLong();

            // 将时间戳和随机数合并，取低64位（8字节）
            long combined = timestamp ^ (randomValue << 32) ^ randomValue;

            // 转换为十六进制字符串并补0
            return String.format("%016x", combined);
        }

        /**
         * 方法4：生成UUID并转换为8字节十六进制
         * 基于UUID的随机性
         */
        public static String generateUuidBasedHex8Bytes() {
            // 获取UUID的最低8字节
            long mostSigBits = java.util.UUID.randomUUID().getMostSignificantBits();
            long leastSigBits = java.util.UUID.randomUUID().getLeastSignificantBits();

            // 使用异或组合两个部分
            long combined = mostSigBits ^ leastSigBits;

            return String.format("%016x", combined);
        }

        /**
         * 核心方法：将字节数组转换为十六进制字符串（不足补0）
         */
        private static String bytesToHex(byte[] bytes) {
            StringBuilder hexString = new StringBuilder();

            for (byte b : bytes) {
                // 每个字节转换为两位十六进制，不足两位前面补0
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        }

        /**
         * 使用ByteBuffer优化性能
         */
        public static String generateHex8BytesWithByteBuffer() {
            SecureRandom secureRandom = new SecureRandom();

            // 使用long类型生成8字节
            long randomLong = secureRandom.nextLong();

            // 直接格式化为16位十六进制
            return String.format("%016x", randomLong);
        }

        /**
         * 验证十六进制字符串是否有效（8字节=16字符）
         */
        public static boolean isValid8ByteHex(String hex) {
            if (hex == null || hex.length() != 16) {
                return false;
            }

            // 验证是否为有效的十六进制字符
            return hex.matches("[0-9a-fA-F]{16}");
        }

        /**
         * 批量生成多个8字节十六进制数
         */
        public static String[] generateMultipleHex8Bytes(int count) {
            if (count <= 0) {
                return new String[0];
            }

            String[] results = new String[count];
            SecureRandom secureRandom = new SecureRandom();

            for (int i = 0; i < count; i++) {
                long randomLong = secureRandom.nextLong();
                results[i] = String.format("%016x", randomLong);
            }

            return results;
        }

        /**
         * 生成指定范围的随机十六进制数（自定义字节数）
         * @param byteCount 字节数（1字节=2字符）
         */
        public static String generateHex(int byteCount) {
            if (byteCount <= 0) {
                throw new IllegalArgumentException("字节数必须大于0");
            }

            SecureRandom secureRandom = new SecureRandom();
            byte[] bytes = new byte[byteCount];
            secureRandom.nextBytes(bytes);

            return bytesToHex(bytes);
        }

        /**
         * 性能测试和示例
         */
        public static void main(String[] args) {
            // 生成单个8字节十六进制数
            System.out.println("方法1 (SecureRandom): " + generateSecureHex8Bytes());
            System.out.println("方法2 (Random): " + generateRandomHex8Bytes());
            System.out.println("方法3 (时间戳): " + generateTimestampHex8Bytes());
            System.out.println("方法4 (UUID): " + generateUuidBasedHex8Bytes());
            System.out.println("方法5 (ByteBuffer): " + generateHex8BytesWithByteBuffer());

            // 验证生成的十六进制数
            String testHex = generateSecureHex8Bytes();
            System.out.println("\n测试验证:");
            System.out.println("生成的十六进制: " + testHex);
            System.out.println("长度: " + testHex.length());
            System.out.println("是否有效: " + isValid8ByteHex(testHex));

            // 转换为大写
            System.out.println("大写: " + testHex.toUpperCase());

            // 批量生成
            System.out.println("\n批量生成5个:");
            String[] multiple = generateMultipleHex8Bytes(5);
            for (String hex : multiple) {
                System.out.println(hex);
            }

            // 生成不同字节数的示例
            System.out.println("\n不同字节数示例:");
            System.out.println("4字节(8字符): " + generateHex(4));
            System.out.println("16字节(32字符): " + generateHex(16));

            // 性能测试
            System.out.println("\n性能测试 (生成10万个):");
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < 100000; i++) {
                generateSecureHex8Bytes();
            }
            long endTime = System.currentTimeMillis();
            System.out.println("耗时: " + (endTime - startTime) + "ms");

            /**
             * 方法1 (SecureRandom): c468c40fe636dc8b
             * 方法2 (Random): 61a4197da58d7e8c
             * 方法3 (时间戳): 332c2d4e54d735e3
             * 方法4 (UUID): eb1cc0bffa20256a
             * 方法5 (ByteBuffer): d08e2d0a1d290fe0
             *
             * 测试验证:
             * 生成的十六进制: 1114512043e951d8
             * 长度: 16
             * 是否有效: true
             * 大写: 1114512043E951D8
             *
             * 批量生成5个:
             * 4498e91702773ffd
             * 821b7c115a82746e
             * 0e5f812e49bf3bef
             * e29084ef193068ce
             * 0f35d348467f41b8
             *
             * 不同字节数示例:
             * 4字节(8字符): 7d24c7da
             * 16字节(32字符): 13bb681dcf79feca0e368dae77fa5b57
             *
             * 性能测试 (生成10万个):
             * 耗时: 4449ms
             *
             * Process finished with exit code 0
             */
        }
    }

    /**
     * 使用Java 8 Stream API生成十六进制数
     */
    static class HexStreamGenerator {

        /**
         * 使用Stream生成8字节十六进制
         */
        public static String generateHex8BytesWithStream() {
            SecureRandom random = new SecureRandom();

            // 生成8个随机字节，转换为十六进制字符串
            return IntStream.range(0, 8)
                    .mapToObj(i -> {
                        int byteValue = random.nextInt(256); // 0-255
                        return String.format("%02x", byteValue);
                    })
                    .collect(Collectors.joining());
        }

        /**
         * 使用Stream生成任意字节数的十六进制
         */
        public static String generateHexWithStream(int byteCount) {
            SecureRandom random = new SecureRandom();

            return IntStream.range(0, byteCount)
                    .mapToObj(i -> String.format("%02x", random.nextInt(256)))
                    .collect(Collectors.joining());
        }

        /**
         * 生成唯一ID（结合时间戳和随机数）
         */
        public static String generateUniqueId() {
            long timestamp = System.currentTimeMillis();
            SecureRandom random = new SecureRandom();

            // 前4字节：时间戳的低4字节
            // 后4字节：随机数
            String timestampHex = String.format("%08x", timestamp & 0xFFFFFFFFL);
            String randomHex = IntStream.range(0, 4)
                    .mapToObj(i -> String.format("%02x", random.nextInt(256)))
                    .collect(Collectors.joining());

            return timestampHex + randomHex;
        }

        public static void main(String[] args) {
            System.out.println("Stream生成8字节: " + generateHex8BytesWithStream());
            System.out.println("Stream生成16字节: " + generateHexWithStream(16));
            System.out.println("唯一ID: " + generateUniqueId());

            // 验证长度
            String hex = generateHex8BytesWithStream();
            System.out.println("\n验证:");
            System.out.println("长度: " + hex.length() + " (应为16)");
            System.out.println("是否十六进制: " + hex.matches("[0-9a-f]{16}"));

            /**
             * Stream生成8字节: a5881862fecce5ad
             * Stream生成16字节: 3c4d858f8aa009591cbc1e51b7abd167
             * 唯一ID: fd729d11a25d6f4f
             *
             * 验证:
             * 长度: 16 (应为16)
             * 是否十六进制: true
             */
        }
    }


}
