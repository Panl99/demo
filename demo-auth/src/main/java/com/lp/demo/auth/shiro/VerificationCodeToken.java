package com.lp.demo.auth.shiro;

import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.authc.HostAuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;

/**
 * @author lp
 * @date 2023/2/22 9:42
 * @desc 验证码认证
 **/
public class VerificationCodeToken implements HostAuthenticationToken, RememberMeAuthenticationToken {

    private static final long serialVersionUID = 5446964288604978404L;

    @Getter
    @Setter
    private String account;
    private boolean rememberMe;
    private String host;

    public VerificationCodeToken() {
    }

    public VerificationCodeToken(String account) {
        this.account = account;
    }

    public VerificationCodeToken(String account, boolean rememberMe) {
        this.account = account;
        this.rememberMe = rememberMe;
    }

    public VerificationCodeToken(String account, boolean rememberMe, String host) {
        this.account = account;
        this.rememberMe = rememberMe;
        this.host = host;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public boolean isRememberMe() {
        return rememberMe;
    }

    @Override
    public Object getPrincipal() {
        return account;
    }

    @Override
    public Object getCredentials() {
        return account;
    }
}
