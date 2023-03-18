package com.lp.demo.auth.shiro.component;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Objects;


public class SessionContext {

    public static final String ADMIN_USER = "admin_user";
    public static final String ADMIN_LOGIN_PASSWORD_ERROR = "admin_login_password_error";
    public static final String SMS_VERIFY_CODE = "sms_verify_code";


    //用來存储RPC调用时的session相关信息
    static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<>();

    /**
     * 获取请求对象
     *
     * @return request
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes == null) {
            return null;
        }
        return servletRequestAttributes.getRequest();
    }

    /**
     * 获取session对象
     *
     * @return session
     */
    public static HttpSession getSession() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        return request.getSession();
    }

    /**
     * 获取sessionId
     *
     * @return sessionId
     */
    public static String getSessionId() {
        HttpSession session = getSession();
        if (null != session) {
            //从http session中获取
            return session.getId();
        } else {
            //从线程本地存储中获取
            Map<String, Object> data = SessionContext.threadLocal.get();
            //判断当前线程是否有数据
            return null != data ? (String) data.get("sessionId") : null;
        }
    }

    /**
     * 获取灰度模式
     *
     * @return greyMode
     */
    public static String getGreyMode() {
        HttpServletRequest request = getRequest();
        if (null != request) {
            //从request中获取
            Cookie[] cookies = request.getCookies();
            String greyMode = null;
            if (cookies != null){
                for (Cookie cookie : cookies) {
                    if (Objects.equals(cookie.getName(), "GREYMODE")) {
                        greyMode = cookie.getValue();
                    }
                }
            }
            return "true".equals(greyMode) ? "true" : "false";
        } else {
            //从线程本地存储中获取
            Map<String, Object> data = SessionContext.threadLocal.get();
            //判断当前线程是否有数据
            return null != data ? (String) data.get("greyMode") : null;
        }
    }

    /**
     * 获取session中保存的属性
     *
     * @param key 属性名
     * @return obj
     */
    public static Object getSessionAttr(String key) {
        HttpSession session = getSession();
        if (session != null) {
            return session.getAttribute(key);
        }
        // 从redis查询session对象中的属性
        HashOperations ops = RedisManager.redisTemplate.opsForHash();
        return ops.get("spring:session:sessions:" + getSessionId(), "sessionAttr:" + key);
    }

    /**
     * 将对象保存到session中
     *
     * @param key   属性名
     * @param value 属性值
     */
    public static void setSessionAttr(String key, Object value) {
        HttpSession session = getSession();
        if (session != null) {
            session.setAttribute(key, value);
        }
        HashOperations ops = RedisManager.redisTemplate.opsForHash();
        ops.put("spring:session:sessions:" + getSessionId(), "sessionAttr:" + key, value);
    }
}
