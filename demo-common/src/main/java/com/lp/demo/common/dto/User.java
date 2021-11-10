package com.lp.demo.common.dto;

import lombok.Data;

import java.util.List;

/**
 * @author lp
 * @date 2021/9/15 17:23
 **/
@Data
public class User {
    Integer userId;
    String userName;
    List<Integer> phoneNumberList;
    Boolean isStudent;
}
