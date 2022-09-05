package com.lp.demo.auth.jwt.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class JwtUserInfo implements Serializable {

    private static final long serialVersionUID = -5818264390645367525L;

    private Long id;
    /**
     * 用户名
     */
    private String name;
    /**
     * 手机号
     */
    private String phone;

    /**
     * 微信用户id
     */
    private String wxUnionId;

    /**
     * 微信小程序用户唯一标识
     */
    private String wxOpenId;

    public JwtUserInfo() {
    }

    public JwtUserInfo(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.phone = user.getPhone();
        this.wxUnionId = user.getWeChatUserInfo().getWxUnionId();
        this.wxOpenId = user.getWeChatUserInfo().getWxOpenId();
    }
}
