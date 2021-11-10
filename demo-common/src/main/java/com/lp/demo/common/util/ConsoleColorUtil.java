package com.lp.demo.common.util;


/**
 * @author lp
 * @date 2021/8/20 13:51
 **/
public class ConsoleColorUtil {

    /**
     * 控制台打印带颜色文本
     * @param content 文本内容
     * @param colorCode 颜色码
     * @param styleCode 样式码
     */
    public static void printSingleColor(String content, int colorCode, int styleCode) {
        System.out.println("[" + DateUtil.getSimpleDateFormat() + "] - [" +Thread.currentThread().getName() + "]  " + "\033["+ colorCode + ";" + styleCode + "m " + content + "\033[0m");
    }

    /**
     * 控制台打印默认样式文本
     * @param content 文本内容
     */
    public static void printDefaultColor(String content) {
        System.out.println("[" + DateUtil.getSimpleDateFormat() + "] - [" +Thread.currentThread().getName() + "]  " + "\033["+ ConsoleColorCodeEnum.GREEN.getColorCode() + ";" + ConsoleStyleCodeEnum.DEFAULT.getStyleCode() + "m " + content + "\033[0m");
    }

    public static void main(String[] args) {
        printSingleColor("wwkokdadl", ConsoleColorCodeEnum.BLACK.getColorCode(), ConsoleStyleCodeEnum.BOLD.getStyleCode());
        printSingleColor("wwkokdadl", ConsoleColorCodeEnum.RED.getColorCode(), ConsoleStyleCodeEnum.ITALIC.getStyleCode());
        printSingleColor("wwkokdadl", ConsoleColorCodeEnum.GREEN.getColorCode(), ConsoleStyleCodeEnum.UNDERSCORE.getStyleCode());
        printSingleColor("wwkokdadl", ConsoleColorCodeEnum.YELLOW.getColorCode(), ConsoleStyleCodeEnum.DEFAULT.getStyleCode());
        printSingleColor("wwkokdadl", ConsoleColorCodeEnum.BLUE.getColorCode(), 1);
        printSingleColor("wwkokdadl", ConsoleColorCodeEnum.PURPLE.getColorCode(), 1);
        printSingleColor("wwkokdadl", ConsoleColorCodeEnum.CYAN.getColorCode(), 1);
        printSingleColor("wwkokdadl", ConsoleColorCodeEnum.GRAY.getColorCode(), 1);
        printSingleColor("wwkokdadl", ConsoleColorCodeEnum.LIGHT_GRAY.getColorCode(), 1);
        printSingleColor("wwkokdadl", ConsoleColorCodeEnum.WHITE.getColorCode(), 1);
    }

    /**
     * 前景色枚举
     * 后景色41-46
     */
    enum ConsoleColorCodeEnum {
        // 白色
        BLACK(30),
        // 红色
        RED(31),
        // 绿色
        GREEN(32),
        // 黄色
        YELLOW(33),
        // 蓝色
        BLUE(34),
        // 紫色
        PURPLE(35),
        // 青色
        CYAN(36),
        // 灰色
        GRAY(37),
        // 浅灰
        LIGHT_GRAY(38),
        // 深灰
//        DARK_GRAY(37)
        WHITE(39);


        private final int colorCode;

        public int getColorCode() {
            return colorCode;
        }
        ConsoleColorCodeEnum(int colorCode) {
            this.colorCode = colorCode;
        }
    }

    enum ConsoleStyleCodeEnum {
        // 加粗
        BOLD(1),
        //
        DEFAULT(2),
        // 斜体
        ITALIC(3),
        // 下划线
        UNDERSCORE(4);

        private final int styleCode;

        public int getStyleCode() {
            return styleCode;
        }
        ConsoleStyleCodeEnum(int styleCode) {
            this.styleCode = styleCode;
        }
    }
}
