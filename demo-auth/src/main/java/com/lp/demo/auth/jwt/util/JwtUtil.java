package com.lp.demo.auth.jwt.util;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lp.demo.auth.jwt.model.JwtUserInfo;
import com.lp.demo.auth.jwt.result.JwtTokenEnum;
import com.lp.demo.common.exception.DisplayableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
public class JwtUtil {

    //密钥
    private static String secret;

    private static String refreshSecret;

    @Value("${jwt.secret}")
    private void setSecret(String secret) {
        JwtUtil.secret = secret;
    }

    @Value("${jwt.refreshSecret}")
    private void setRefreshSecret(String refreshSecret) {
        JwtUtil.refreshSecret = refreshSecret;
    }

    /**
     * 生成token
     *
     * @param user 用户
     * @return token
     */
    public static String createToken(JwtUserInfo user, Integer expire) {
        return JWT.create()
                .setExpiresAt(getExpiresDate(expire))
                .setKey(secret.getBytes())
                .setPayload("user", user)
                .sign();
    }

    /**
     * 生成刷新token
     *
     * @param user 用户
     * @return token
     */
    public static String createRefToken(JwtUserInfo user, Integer expire) {
        return JWT.create()
                .setExpiresAt(getExpiresDate(expire))
                .setKey(refreshSecret.getBytes())
                .setPayload("user", user)
                .sign();
    }

    private static Date getExpiresDate(Integer expire) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, expire);
        return calendar.getTime();
    }

    /**
     * 获取token中的负载信息
     *
     * @param token token
     * @return 负载信息
     */
    public static JwtUserInfo getUser(String token) {
        return getUser(JWT.of(token).setKey(secret.getBytes()));
    }

    /**
     * 获取token中的负载信息
     *
     * @param jwt jwt
     * @return 负载信息
     */
    public static JwtUserInfo getUser(JWT jwt) {
        JWTPayload payload = jwt.getPayload();
        return JSONObject.parseObject(JSON.toJSONString(payload.getClaim("user")), JwtUserInfo.class);
    }

    /**
     * 校验token是否超时
     *
     * @param jwt jwt
     * @return DecodedJWT
     */
    public static boolean validate(JWT jwt) {
        return jwt.validate(0);
    }

    /**
     * 验证token是否正确
     *
     * @param jwt
     * @return
     */
    public static boolean verify(JWT jwt) {
        return jwt.verify();
    }

    /**
     * 根据token生成jwt对象
     *
     * @param token
     * @return
     */
    public static JWT ofToken(String token) {
        try {
            return JWT.of(token).setKey(secret.getBytes());
        } catch (Exception e) {
            throw new DisplayableException(JwtTokenEnum.TOKEN_ERROR);
        }
    }

    /**
     * 根据refToken生成jwt对象
     *
     * @param refToken 刷新token
     * @return
     */
    public static JWT ofRefToken(String refToken) {
        try {
            return JWT.of(refToken).setKey(refreshSecret.getBytes());
        } catch (Exception e) {
            throw new DisplayableException(JwtTokenEnum.TOKEN_ERROR);
        }
    }
}
