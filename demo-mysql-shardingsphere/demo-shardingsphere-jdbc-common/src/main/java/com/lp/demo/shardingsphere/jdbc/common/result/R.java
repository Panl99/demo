package com.lp.demo.shardingsphere.jdbc.common.result;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.Objects;

@Data
public class R<T> {

    private long timestamp = System.currentTimeMillis();

    private int code;

    private String msg;

    private T data;

    public R() {
    }

    public static <T> R<T> success(T t) {
        R<T> result = new R<>();
        result.setCode(BaseCode.SUCCESS.getCode());
        result.setMsg(BaseCode.SUCCESS.getMsg());
        result.setData(t);
        return result;
    }

    public static <T> R<T> success(T t, String msg) {
        R<T> result = success(t);
        result.setMsg(msg);
        return result;
    }

    public static R fail(BaseEnum codeEnum) {
        R result = new R();
        result.setCode(codeEnum.getCode());
        result.setMsg(codeEnum.getMsg());
        return result;
    }

    public static R fail(BaseEnum codeEnum, String msg) {
        R result = new R();
        result.setCode(codeEnum.getCode());
        result.setMsg(msg);
        return result;
    }

    public static R fail(String msg) {
        R result = new R();
        result.setCode(BaseCode.FAIL.getCode());
        result.setMsg(msg);
        return result;
    }

    public static <T> R<T> fail(T t, String msg) {
        R<T> result = new R<>();
        result.setCode(BaseCode.FAIL.getCode());
        result.setMsg(msg);
        result.setData(t);
        return result;
    }

    public static <T> R<T> res(BaseEnum codeEnum, T t) {
        R<T> result = new R<>();
        result.setMsg(codeEnum.getMsg());
        result.setCode(codeEnum.getCode());
        result.setData(t);
        return result;
    }

    public static R res(Integer code, String msg) {
        R result = new R<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public boolean isSuccess() {
        if (Objects.equals(BaseCode.SUCCESS.getCode(), this.getCode())) {
            return true;
        } else {
            return false;
        }
    }

    public String toJsonString() {
        return JSONObject.toJSONString(this);
    }
}

