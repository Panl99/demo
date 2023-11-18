package com.lp.demo.shardingsphere.jdbc.common.result;

import java.util.Optional;

public enum BaseCode implements BaseEnum<BaseCode> {

    /**
     *
     */
    SUCCESS(200, "操作成功"),
    FAIL(-1, "操作失败"),
    ;

    private final Integer code;
    private final String msg;

    BaseCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }

    public static Optional<BaseCode> of(Integer code) {
        return Optional.ofNullable(BaseEnum.parseByCode(BaseCode.class, code));
    }
}
