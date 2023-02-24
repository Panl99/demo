package com.lp.demo.auth.shiro.interceptor;

import com.alibaba.fastjson.JSON;
import com.lp.demo.auth.shiro.component.SessionContext;
import com.lp.demo.auth.shiro.entity.AdminUser;
import com.lp.demo.auth.shiro.handler.PlatformExceptionHandler;
import com.lp.demo.common.result.JsonResult;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 后台登陆检查拦截器
 */
@Component
public class LoginCheckInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断不是静态资源访问
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            //判断是否有需要登录的注解，并判断session中是否有userinfo
            if (method.hasMethodAnnotation(RequiresAuthentication.class)) {
                AdminUser adminUser = (AdminUser) (SessionContext.getSessionAttr(SessionContext.ADMIN_USER));
                if (adminUser == null) {
                    response.setHeader("Content-Type", "application/json;charset=UTF-8");
                    JsonResult result = new JsonResult();
                    result.setCode(PlatformExceptionHandler.TEMP_USER_ERROR);
                    result.setMsg("状态异常，用户未登录");
                    response.getWriter().print(JSON.toJSONString(result));
                    return false;
                }
            }
        }
        return true;
    }
}
