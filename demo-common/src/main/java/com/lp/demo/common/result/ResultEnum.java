package com.lp.demo.common.result;

import org.omg.CORBA.TIMEOUT;

/**
 * @author lp
 * @date 2021/9/8 11:36
 **/
public enum ResultEnum {
    /**
     * 成功
     */
    SUCCESS(200, "success"),
    /**
     * 失败
     */
    FAIL(500, "fail"),
    /**
     * 超时
     */
    TIMEOUT(502, "timeout");

    private int code;
    private String msg;

    ResultEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
