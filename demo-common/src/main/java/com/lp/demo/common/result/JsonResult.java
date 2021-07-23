package com.lp.demo.common.result;

import cn.hutool.json.JSONUtil;
import com.lp.demo.common.dto.UserDto;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author liupan
 * @date 2021/7/16 17:32
 **/
@Data
public class JsonResult<T> implements Serializable {
    private final long timestamp = System.currentTimeMillis();
    private int code = 2000;
    private String msg;
    private T data;

    public JsonResult() {
    }

    public JsonResult(T data) {
        this.data = data;
    }

    public JsonResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public JsonResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


    public static void main(String[] args) {
        JsonResult<UserDto> result1 = new JsonResult<>();
        System.out.println(JSONUtil.parse(result1).toStringPretty());

        JsonResult<UserDto> result2 = new JsonResult<>(2001, "getMsg");
        System.out.println(JSONUtil.parse(result2).toStringPretty());

        JsonResult<UserDto> result3 = new JsonResult<>(2002, "getMsg", UserDto.initUserDto());
        System.out.println(JSONUtil.parse(result3).toStringPretty());

        JsonResult<UserDto> result4 = new JsonResult<>(UserDto.initUserDto());
        System.out.println(JSONUtil.parse(result4).toStringPretty());

        System.out.println(new Date(1626486228144L));
    }
}
