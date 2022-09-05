package com.lp.demo.auth.jwt.model;

import lombok.Data;

import java.util.Date;

/**
 * @author lp
 * @date 2022/9/5 11:40
 * @desc
 **/
@Data
public class User {
    private Long id;
    private String name;
    private String password;
    private String nickname;
    private String phone;
    private String icon;
    private Date birthday;
    /**
     * 性别(0未知，1男，2女)
     */
    private Integer gender;
    /**
     * 省/直辖市
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 区
     */
    private String region;
    /**
     * 详细地址
     */
    private String detailAddress;
    private String remark;
    /**
     * 微信用户信息
     */
    private WechatUserInfo weChatUserInfo;
    private Boolean status;

}
