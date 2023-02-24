package com.lp.demo.auth.shiro.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class LoginDto implements Serializable {

    private static final long serialVersionUID = -8678945780679040054L;

    /**
     * 账号：用户名、邮箱、手机号
     */
    @NotBlank
    private String account;
    /**
     * Base64(密码)
     */
    private String password;
    /**
     * 短信/邮箱 验证码
     */
    private String verificationCode;
    /**
     * 图片验证码
     */
    private String imageVerificationCode;
    /**
     * 应用唯一标识 sdk keyId
     */
    private String keyId;
    /**
     * 登录方式
     */
    @NotNull
    private LoginTypeEnum loginType;

    public enum LoginTypeEnum {
        /**
         * 密码
         */
        PASSWORD,
        /**
         * 验证码
         */
        VERIFICATION_CODE,
        ;
    }
}
