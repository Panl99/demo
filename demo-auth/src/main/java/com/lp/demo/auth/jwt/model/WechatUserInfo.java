package com.lp.demo.auth.jwt.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lp
 * @date 2022/9/5 11:45
 * @desc
 **/
@Data
public class WechatUserInfo implements Serializable {

    private static final long serialVersionUID = 6100430810176936070L;

    /**
     * 微信用户id
     */
    private String wxUnionId;

    /**
     * 微信小程序用户唯一标识
     */
    private String wxOpenId;
}
