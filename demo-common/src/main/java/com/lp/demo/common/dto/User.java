package com.lp.demo.common.dto;

import lombok.Data;

/**
 * @author lp
 * @date 2021/9/15 17:23
 **/
@Data
public class User {
    Integer userId;
    String userName;
    Integer phoneNumber;
    Boolean isStudent;
}
