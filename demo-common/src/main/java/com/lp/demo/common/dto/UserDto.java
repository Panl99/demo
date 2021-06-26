package com.lp.demo.common.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lp
 * @date 2021/5/17 23:53
 * @description
 **/
@Data
public class UserDto {
    private String name;
    private Integer age;
    private Integer phoneNumber;
    private String address;
    private Date date;
    private List<String> hobby;
    private Map<String, String> school;

    public static UserDto initUserDto() {
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
