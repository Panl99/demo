package com.lp.demo.common.util.hutool;

import cn.hutool.core.lang.Console;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.lp.demo.common.dto.UserDto;

/**
 * @author liupan
 * @date 2021/7/16 15:49
 **/
public class JsonUtilDemo {
    private static String jsonArrayStr = "[\"value1\", \"value2\", \"value3\"]";
    private static String jsonObjectStr = "{\"b\":\"value2\",\"c\":\"value3\",\"a\":\"value1\"}";

    public static void main(String[] args) {
        createJsonArrayTest();
        parseJsonArrayTest();

        createJsonObject();
        parseJsonObjectTest();

        parseJsonObjectToStrTest();

        parseBeanToJsonObject();
        parseJsonObjectToBean();
    }

    /**
     * 创建 JsonArray
     */
    public static void createJsonArrayTest() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>createJsonArrayTest()<<<<<<<<<<<<<<<<<<<<<<<<<<");
        //
        JSONArray array = JSONUtil.createArray();
        System.out.println(array);
        //
        JSONArray jsonArray = new JSONArray();
        System.out.println(jsonArray);
    }

    /**
     * 转换
     * str -> JsonArray
     */
    public static void parseJsonArrayTest() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>parseJsonArrayTest()<<<<<<<<<<<<<<<<<<<<<<<<<<");
        JSONArray objects = JSONUtil.parseArray(jsonArrayStr);
        System.out.println(objects); // ["value1","value2","value3"]
    }

    /**
     * 创建 JsonObject
     */
    public static void createJsonObject() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>createJsonObject()<<<<<<<<<<<<<<<<<<<<<<<<<<");
        JSONObject json1 = JSONUtil.createObj()
                .set("a", "value1")
                .set("b", "value2")
                .set("c", "value3");
        System.out.println(json1);
        //
        JSONObject json2 = new JSONObject();
        System.out.println(json2);
    }

    /**
     * 转换
     * str -> JsonObject
     */
    public static void parseJsonObjectTest() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>parseJsonObjectTest()<<<<<<<<<<<<<<<<<<<<<<<<<<");
        //方法一：使用工具类转换
        JSONObject jsonObject = JSONUtil.parseObj(jsonObjectStr);
        System.out.println(jsonObject);
        //方法二：new的方式转换
        JSONObject jsonObject2 = new JSONObject(jsonObjectStr);
        System.out.println(jsonObject2);
    }

    /**
     * 转换
     * JsonObject -> str
     */
    public static void parseJsonObjectToStrTest() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>parseJsonObjectToStrTest()<<<<<<<<<<<<<<<<<<<<<<<<<<");
        JSONObject jsonObject = JSONUtil.parseObj(jsonObjectStr);
        System.out.println(jsonObject.toString());
        System.out.println(jsonObject.toStringPretty());
    }

    /**
     * 转换
     * Bean -> JsonObject
     */
    public static void parseBeanToJsonObject() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>parseBeanToJsonObject()<<<<<<<<<<<<<<<<<<<<<<<<<<");
        UserDto userDto = UserDto.initUserDto();
        // false表示不跳过空值
        JSONObject json = JSONUtil.parseObj(userDto, false);
        // 第二个参数true表示转换后JsonObject中Bean字段保持有序
//        JSONObject json = JSONUtil.parseObj(userDto, false, true);
        // 默认的，Hutool将日期输出为时间戳，如果需要自定义日期格式，可以调用： TODO:没生效？
        json.setDateFormat("yyyy-MM-dd HH:mm:ss");
        Console.log(json.toStringPretty());
/*        {
            "date": 1626423147573,
                "address": "广东",
                "phoneNumber": 123456789,
                "school": {
                    "中学": "庞各庄一中",
                            "大学": "庞各庄大一",
                            "小学": "庞各庄一小"
                },
                "name": "zhangsan",
                "age": 18,
                "hobby": [
                    "篮球",
                    "羽毛球",
                    "爬山"
                ]
        }*/
    }

    /**
     * 转换
     * JsonObject -> Bean
     */
    public static void parseJsonObjectToBean() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>parseJsonObjectToBean()<<<<<<<<<<<<<<<<<<<<<<<<<<");
        UserDto userDto = UserDto.initUserDto();
        // false表示不跳过空值
        JSONObject json = JSONUtil.parseObj(userDto, false);

        UserDto userDto1 = JSONUtil.toBean(json, UserDto.class);
        Console.log(userDto1);

    }

}
