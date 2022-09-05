package com.lp.demo.common.exception;

import com.lp.demo.common.result.BaseEnum;

import java.util.Arrays;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lp
 * @date 2021/9/8 11:28
 **/
public class DisplayableException extends RuntimeException {
    private static Pattern p = Pattern.compile("\\{(\\d*?)\\}");

    public DisplayableException(int code, String msg) {
        super(code + "|" + msg);
    }

    public DisplayableException(String msg) {
        super(msg);
    }

    public DisplayableException(BaseEnum result) {
        super(result.getCode() + "|" + result.getName());
    }

    public DisplayableException(BaseEnum result, String... args) {
        super(result.getCode() + "|" + fillParameter(result.getName(), args));
    }

    /**
     * 填充参数，将参数数组填充到{0}，{1}....中
     */
    public static String fillParameter(String msg, String[] args) {

        if (args == null || args.length == 0) {
            return msg;
        }

        String[] msgSplit = p.split(msg);

        StringBuilder msgBuffer = new StringBuilder();
        ListIterator<String> msgIterator = Arrays.asList(msgSplit).listIterator();

        if (msgIterator.hasNext()) {
            msgBuffer.append(msgIterator.next());
        }

        Matcher matcher = p.matcher(msg);
        while (matcher.find()) {

            int index = Integer.parseInt(matcher.group(1));

            if (args.length > index) {
                msgBuffer.append(args[index]);
            } else {
                msgBuffer.append(matcher.group());
            }
            if (msgIterator.hasNext()) {
                msgBuffer.append(msgIterator.next());
            }
        }
        return msgBuffer.toString();
    }
}
