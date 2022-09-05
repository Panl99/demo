package com.lp.demo.common.result;

import java.util.Optional;

/**
 * @author lp
 * @date 2021/9/8 11:36
 **/
public enum ResultEnum implements BaseEnum<ResultEnum> {
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

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getName() {
        return msg;
    }

    public String getMsg() {
        return msg;
    }



    public static Optional<ResultEnum> of(Integer code) {
        return Optional.ofNullable(BaseEnum.parseByCode(ResultEnum.class, code));
    }
}
