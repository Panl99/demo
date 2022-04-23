package com.lp.demo.common.interceptor;

import com.lp.demo.common.dto.OperationLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lp
 * @date 2022/4/20 12:13
 **/
@Slf4j
public class OperatorLogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        OperationLog operationLog = LogUtil.getLog(request, (HandlerMethod) handler);
        request.setAttribute(LogUtil.LOG_OPERATE, operationLog);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        OperationLog operationLog = (OperationLog) request.getAttribute(LogUtil.LOG_OPERATE);
        if (operationLog == null) {
            log.warn("operation log is null!");
            return;
        }

        // TODO 插入操作日志到数据库
    }
}
