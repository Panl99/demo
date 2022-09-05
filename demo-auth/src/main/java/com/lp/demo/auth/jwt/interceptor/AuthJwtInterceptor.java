package com.lp.demo.auth.jwt.interceptor;

import cn.hutool.jwt.JWT;
import com.lp.demo.auth.jwt.annotation.AuthJwt;
import com.lp.demo.auth.jwt.result.JwtTokenEnum;
import com.lp.demo.auth.jwt.util.JwtUtil;
import com.lp.demo.common.exception.DisplayableException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class AuthJwtInterceptor implements HandlerInterceptor {

    public static final String JWT_TOKEN = "X-Auth-JwtToken";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            //有Auth注解
            if (method.hasMethodAnnotation(AuthJwt.class)) {
                //鉴权
                auth(request);
            }
        }
        return true;
    }

    private void auth(HttpServletRequest request) {
        String jwtToken = request.getHeader(JWT_TOKEN);
        if (StringUtils.isEmpty(jwtToken)) {
            log.info("请求头没有jwtToken");
            throw new DisplayableException(JwtTokenEnum.TOKEN_ERROR);
        }

        //JWT鉴权
        JWT jwt = JwtUtil.ofToken(jwtToken);
        if (!JwtUtil.verify(jwt)){
            log.info("无效的token:{}",jwtToken);
            throw new DisplayableException(JwtTokenEnum.TOKEN_ERROR);
        }
        if (!JwtUtil.validate(jwt)){
            log.info("token已过期:{}",jwtToken);
            throw new DisplayableException(JwtTokenEnum.TOKEN_EXPIRED);
        }
    }
}
