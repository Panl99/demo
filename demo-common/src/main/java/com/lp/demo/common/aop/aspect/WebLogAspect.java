package com.lp.demo.common.aop.aspect;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;


/**
 * WEB输入输出日志统一处理类
 */
@Aspect
@Component
@Slf4j
public class WebLogAspect {

    @Pointcut("@annotation(com.lp.demo.common.annotation.WebLog)")
    public void webLog() {
    }

    @Around(value = "webLog()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取执行方法的类的名称（包名加类名）
        String className = joinPoint.getTarget().getClass().getSimpleName();
        // 获取实例和方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String requestId = request.getHeader(ConstantHeaderUtil.REQUEST_ID);
        StringBuilder builder = new StringBuilder();

        Parameter[] parameters = method.getParameters();
        Object[] arguments = joinPoint.getArgs();
        int i = 0;
        try {
            if (parameters != null && arguments != null) {
                for (; i < Math.min(parameters.length, arguments.length); i++) {
                    if (arguments[i] instanceof HttpServletRequest) {
                        appendRequestHeader(request, builder);
                    }else if (arguments[i] instanceof HttpServletResponse){
                        continue;
                    }else if (arguments[i] instanceof MultipartFile){
                        builder.append(parameters[i].getName()).append("=").append(((MultipartFile)arguments[i]).getOriginalFilename()).append(",");
                    }else {
                        builder.append(parameters[i].getName()).append("=").append(JSON.toJSONString(arguments[i])).append(",");
                    }
                }
            }
            if (builder.length() > 0) {
                builder.deleteCharAt(builder.length() - 1);
            }
            log.info("{}.{} request:{},{}", className, method.getName(), requestId, builder.toString());
        }catch (Exception e){
            log.info("{}.{} request:{},参数{}无法被读取", className, method.getName(), requestId, parameters[i]);
        }

        //执行方法获取返回值
        Object proceed = joinPoint.proceed();
        //打印返回值参数
        log.info("{}.{} response:{},{}", className,method.getName(),requestId, JSON.toJSONString(proceed));
        // 返回
        return proceed;
    }

    private void appendRequestHeader(HttpServletRequest request, StringBuilder builder) {
        Field[] fields = ConstantHeaderUtil.class.getFields();
        builder.append("requestHeader{");
        boolean isEmpty = true;
        try {
            for (Field field : fields) {
                String fieldName = String.valueOf(field.get(ConstantHeaderUtil.class));
                String fieldValue = request.getHeader(fieldName);
                if (StringUtils.isNotEmpty(fieldValue)) {
                    builder.append(fieldName).append("=").append(fieldValue).append(",");
                    isEmpty = false;
                }
            }
        } catch (IllegalAccessException e) {
            log.error("异常", e);
        }

        if (!isEmpty) {
            builder.deleteCharAt(builder.length() - 1);
        }
        builder.append("},");
    }

    static class ConstantHeaderUtil {
        public static final String REQUEST_ID = "X-Test-RequestId";
        public static final String KEY_ID = "X-Test-KeyId";
        public static final String SIGNATURE = "X-Test-Signature";
        public static final String AUTHENTICATION = "Authentication";
        public static final String TIME_STAMP = "Time-stamp";
        public static final String DEVICE_TOKEN = "deviceToken";
        public static final String LANGUAGE = "Accept-Language";
        public static final String CLIENT_ID ="X-Test-ClientId";
        public static final String AppVersion ="X-Test-AppVersion";

        /**
         * app info，格式:appid, version, name
         */
        public static final String APPINFO = "X-Test-AppInfo";
        public static final String APP_ID ="X-Test-AppId";
        public static final String PLATFORM ="X-Test-Platform";
        public static final String TEMPORARY_TOKEN ="X-Test-TemporaryToken";
        public static final String PHONE_MODEL ="Phone-Model";
        public static final String ACCESS_KEY = "X-Test-AccessKey";
        public static final String ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
        public static final String ACCEPT_ENCODING= "X-Test-Accept-Encoding";
        public static final String CONTENT_ENCODING= "X-Test-Content-Encoding";
        public static final String JWT_TOKEN= "X-Test-JwtToken";
        public static final String WECHAT_APP_ID = "X-Wechat-AppId";
        public static final String DDING_APP_ID = "X-DDing-AppId";
    }
}
