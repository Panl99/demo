package com.lp.demo.auth.jwt.result;

import com.lp.demo.common.result.BaseEnum;

import java.util.Optional;

/**
 * @author lp
 * @date 2022/9/5 10:55
 * @desc
 **/
public enum JwtTokenEnum implements BaseEnum<JwtTokenEnum> {

    // token错误
    TOKEN_ERROR(70000004, "token error"),
    // token已过期
    TOKEN_EXPIRED(70000005, "token expired");

    private int code;
    private String msg;

    JwtTokenEnum(int code, String msg) {
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

    public static Optional<JwtTokenEnum> of(Integer code) {
        return Optional.ofNullable(BaseEnum.parseByCode(JwtTokenEnum.class, code));
    }
}
