package com.lp.demo.auth.shiro.controller;

import cn.hutool.crypto.SecureUtil;
import com.lp.demo.auth.shiro.VerificationCodeToken;
import com.lp.demo.auth.shiro.component.SessionContext;
import com.lp.demo.auth.shiro.dto.LoginDto;
import com.lp.demo.auth.shiro.entity.AdminUser;
import com.lp.demo.common.annotation.WebLog;
import com.lp.demo.common.exception.DisplayableException;
import com.lp.demo.common.util.StringUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@RestController
@Slf4j
public class LoginController {

    /**
     * 后台用户登录
     * 支持：密码、验证码登录
     */
    @WebLog
    @PostMapping("/user/v1/login")
    public AdminUser loginV2(@RequestBody @Validated LoginDto loginDto) {
        // 图片验证码校验
        verifyImageVerificationCode(loginDto.getImageVerificationCode());

        LoginDto.LoginTypeEnum loginType = loginDto.getLoginType();
        Subject subject = SecurityUtils.getSubject();
        switch (loginType) {
            case PASSWORD:
                String password = loginDto.getPassword();
//                password = Base64Util.baseConvertStr(password);
                subject.login(new UsernamePasswordToken(loginDto.getAccount(), SecureUtil.md5(password)));
                break;
            case VERIFICATION_CODE:
                if (!verifyVerificationCode(loginDto.getVerificationCode())) {
                    throw new DisplayableException("VERIFY_CODE_ERROR");
                }
                subject.login(new VerificationCodeToken(loginDto.getAccount()));
                break;
            default:
                throw new DisplayableException("NOT_SUPPORT");
        }

        AdminUser adminUser = (AdminUser) SessionContext.getSessionAttr(SessionContext.ADMIN_USER);
        if (adminUser.getFrozen()) {
            throw new DisplayableException("账号被冻结");
        }
        // 更新登陆时间
//        adminUserService.updateLastLoginTime(adminUser.getId());
        adminUser.setPassword(null);
        if (subject.isAuthenticated()) {
            SessionContext.setSessionAttr(SessionContext.ADMIN_LOGIN_PASSWORD_ERROR, null);
        }
        return adminUser;
    }

    private void verifyImageVerificationCode(String imageVerificationCode) {
        Object session = SessionContext.getSessionAttr(SessionContext.ADMIN_LOGIN_PASSWORD_ERROR);
        if (Objects.isNull(session)) {
            return;
        }

        if (StringUtil.isEmpty(imageVerificationCode)) {
            throw new DisplayableException("VERIFY_CODE_ERROR");
        }
        Object simpleCaptcha = SessionContext.getSessionAttr("simpleCaptcha");
        log.info("Compare verification code, param code: {}, session code：{}", imageVerificationCode, simpleCaptcha);
        if (!Objects.equals(simpleCaptcha, imageVerificationCode.toLowerCase())) {
            throw new DisplayableException("VERIFY_CODE_ERROR");
        }
    }

    public Boolean verifyVerificationCode(String code) {
        SmsVerificationCode smsVerificationCode = (SmsVerificationCode) SessionContext.getSessionAttr(SessionContext.SMS_VERIFY_CODE);
        if (!Optional.ofNullable(smsVerificationCode).isPresent()) {
            return false;
        }
        // 短信验证码有效期5分钟
        if ((System.currentTimeMillis() - smsVerificationCode.getSendTime().getTime()) / 1000 / 60 > 5) {
            throw new DisplayableException("短信验证码已过期");
        }
        return Objects.equals(code, smsVerificationCode.getVerifyCode());
    }

    /**
     * 用户退出登录
     */
    @WebLog
    @PostMapping("/user/v1/logout")
    public void logout(HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        request.getSession().invalidate();
    }

    @Data
    static class SmsVerificationCode implements Serializable {
        private static final long serialVersionUID = 4406111247559783508L;
        private String phone;
        private String verifyCode;
        private Date sendTime;
    }
}
