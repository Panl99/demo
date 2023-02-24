package com.lp.demo.auth.shiro;

import com.alibaba.fastjson.JSON;
import com.lp.demo.common.result.JsonResult;
import org.apache.shiro.web.filter.authc.UserFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class LoginFilter extends UserFilter {
    @Override
    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        JsonResult result = new JsonResult();
        result.setCode(404);
        result.setMsg("用户未登录");
        response.getWriter().print(JSON.toJSON(result));
    }
}
