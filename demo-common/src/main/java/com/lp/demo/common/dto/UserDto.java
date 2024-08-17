package com.lp.demo.common.dto;

import com.lp.demo.common.config.Enum2AllowableValues;
import com.lp.demo.common.enums.OperationEventTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = "用户")
@Data
public class UserDto {
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "年龄")
    private Integer age;
    @ApiModelProperty(value = "手机号")
    private Integer phoneNumber;
    @ApiModelProperty(value = "地址")
    private String address;
    @ApiModelProperty(value = "日期")
    private Date date;
    @ApiModelProperty(value = "爱好")
    private List<String> hobby;
    @ApiModelProperty(value = "学校")
    private Map<String, String> school;

//    @ApiModelProperty(value = "操作类型", allowableValues = "1:新增,2:修改,3:删除,4:查询") // 原使用方式，需要手动枚举文档
    @ApiModelProperty(value = "操作类型")
    @Enum2AllowableValues(value = OperationEventTypeEnum.class, method = "getCode:getName")
    private String operationEventType;

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
