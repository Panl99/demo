package com.lp.demo.common.dto;

import lombok.Data;

import java.util.Date;
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
}
