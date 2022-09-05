package com.lp.demo.auth.jwt.controller;

import cn.hutool.jwt.JWT;
import com.lp.demo.auth.jwt.annotation.AuthJwt;
import com.lp.demo.auth.jwt.interceptor.AuthJwtInterceptor;
import com.lp.demo.auth.jwt.model.JwtToken;
import com.lp.demo.auth.jwt.model.JwtUserInfo;
import com.lp.demo.auth.jwt.model.JwtUserVO;
import com.lp.demo.auth.jwt.model.User;
import com.lp.demo.auth.jwt.result.JwtTokenEnum;
import com.lp.demo.auth.jwt.service.UserService;
import com.lp.demo.auth.jwt.util.JwtUtil;
import com.lp.demo.common.exception.DisplayableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;

/**
 * @author lp
 * @date 2022/9/5 11:59
 * @desc
 **/
@RestController
@Validated
//@RefreshScope
@Slf4j
@RequestMapping("user/v1")
public class UserController {

    @Autowired
    UserService userService;

    //过期时间(秒)
    @Value("${jwt.expire:3600}")
    private Integer expire;

    public static final String WECHAT_APP_ID = "X-Wechat-AppId";

    /**
     * 微信授权登入
     *
     * @param code          登入授权临时票据
     * @param encryptedData 包括敏感数据在内的完整用户信息的加密数据
     * @param iv            加密算法的初始向量
     * @return token
     */
    @PostMapping("/wechatLogin")
    public JwtUserVO weChatLogin(@RequestParam @NotBlank String code,
                                 @RequestParam(required = false) String encryptedData,
                                 @RequestParam(required = false) String iv,
                                 @RequestHeader(WECHAT_APP_ID) String appId,
                                 HttpServletRequest request) {

        User user = userService.wxOAuth(code, appId, encryptedData, iv);
        return new JwtUserVO(user, expire);
    }

    /**
     * 刷新token
     *
     * @param refreshToken 刷新token
     * @return
     */
    @PostMapping("/refreshToken")
    public JwtToken refreshToken(@RequestParam @NotBlank String refreshToken) {

        JWT jwt = JwtUtil.ofRefToken(refreshToken);
        if (!JwtUtil.verify(jwt)) {
            throw new DisplayableException(JwtTokenEnum.TOKEN_ERROR);
        }
        if (!JwtUtil.validate(jwt)) {
            throw new DisplayableException(JwtTokenEnum.TOKEN_EXPIRED);
        }
        JwtUserInfo jwtUserInfo = JwtUtil.getUser(jwt);

        JwtToken token = new JwtToken();
        token.setToken(JwtUtil.createToken(jwtUserInfo, expire));
        token.setRefreshToken(JwtUtil.createRefToken(jwtUserInfo, expire * 2));
        return token;
    }

    /**
     * 会员用户信息
     *
     * @return
     */
    @AuthJwt
    @GetMapping("/info")
    public User memberInfo(@RequestHeader(AuthJwtInterceptor.JWT_TOKEN) String jwtToken) {
        JwtUserInfo jwtUserInfo = JwtUtil.getUser(jwtToken);
        return userService.userInfo(jwtUserInfo.getId());
    }
}
