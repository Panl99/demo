package com.lp.demo.common.util;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.lp.demo.common.dto.UserDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lp
 * @date 2021/5/17 22:10
 * @description
 **/
public class JsonUtil {

    public static void main(String[] args) {
        String jsonStr = "{\"b\":\"value2\",\"c\":\"value3\",\"a\":\"value1\"}";

        UserDto user = initUserDto();

        JSONObject jsonObject = createJsonObj();

        // json字符串解析

        //方法一：使用工具类转换
        JSONObject parseObj1 = JSONUtil.parseObj(jsonStr, false, true);//false表示不跳过空值, true表示保持顺序
        //方法二：new的方式转换
        JSONObject parseObj2 = new JSONObject(jsonStr);
        System.out.println(parseObj1);
        System.out.println(parseObj2);

        // json转字符串
        String json2Str = parseObj1.toString();
        System.out.println(json2Str);
        // json转字符串，美化带缩进
        String json2StrPretty = parseObj1.toStringPretty();
        System.out.println(json2StrPretty);

        // json转对象
        TestObj testObj = JSONUtil.toBean(jsonObject, TestObj.class);
        System.out.println(testObj);

        // 对象转json
        JSON bean2json = JSONUtil.parse(user);
        JSON bean2jsonConfig = JSONUtil.parse(user, JSONConfig.create());
        System.out.println(bean2json);
        System.out.println(bean2jsonConfig);

        String bean2jsonStringPretty = JSONUtil.parse(user).toStringPretty();
        System.out.println(bean2jsonStringPretty);

    }

    @Data
    static class TestObj {
        private String s1;
        private String s2;
        private String s3;
    }

    private static JSONObject createJsonObj() {
        JSONObject json = JSONUtil.createObj()
                .set("s1", "value-a")
                .set("s2", "value-b")
                .set("s3", "value-c");
        return json;
    }

    private static UserDto initUserDto() {
        UserDto userDto = new UserDto();
        userDto.setName("zhangsan");
        userDto.setAge(18);
        userDto.setAddress("广东");
        userDto.setDate(new Date());
        List<String> hobby = new ArrayList<>();
        hobby.add("篮球");
        hobby.add("羽毛球");
        hobby.add("爬山");
        userDto.setHobby(hobby);
        userDto.setPhoneNumber(123456789);
        Map<String, String> school = new HashMap<>();
        school.put("小学", "庞各庄一小");
        school.put("中学", "庞各庄一中");
        school.put("大学", "庞各庄大一");
        userDto.setSchool(school);

        return userDto;
    }


}
