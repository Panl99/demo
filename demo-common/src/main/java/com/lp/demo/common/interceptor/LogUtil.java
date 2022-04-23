package com.lp.demo.common.interceptor;

import com.lp.demo.common.dto.OperationLog;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author lp
 * @date 2022/4/20 17:31
 **/
public class LogUtil {

    public static final String LOG_OPERATE = "operation";

    public static OperationLog getLog(HttpServletRequest request, HandlerMethod handlerMethod) {
        String scene;
        Method method = handlerMethod.getMethod();
        Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
        for (Annotation annotation : declaredAnnotations) {
            Class<? extends Annotation> clazz = annotation.annotationType();
            // TODO 获取操作接口描述，什么操作
        }

        OperationLog operationLog = new OperationLog();
        // TODO 根据上一步获取设置参数
//        operationLog.setId();
//        operationLog.setUserId();
//        operationLog.setOperator();
//        operationLog.setScene();
//        operationLog.setRemoteAddress();
//        operationLog.setRequest();
//        operationLog.setResponse();
//        operationLog.setCreateTime();
        return operationLog;
    }
}
