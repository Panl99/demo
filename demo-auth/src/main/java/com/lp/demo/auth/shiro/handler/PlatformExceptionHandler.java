package com.lp.demo.auth.shiro.handler;

import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.lp.demo.auth.shiro.component.SessionContext;
import com.lp.demo.common.exception.DisplayableException;
import com.lp.demo.common.result.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Objects;

@ControllerAdvice
@Slf4j
public class PlatformExceptionHandler {

    public final static int TEMP_PASSWORD_ERROR = 1001001;
    public final static int TEMP_PERMISSION_DENIED = 1001002;
    public final static int TEMP_USER_ERROR = 1001003;
    public final static int TEMP_ACCOUNT_NOT_EXIST = 1001004;
    public final static int TEMP_PARAMS_ERROR = 1001005;
    public final static int TEMP_SERVER_ERROR = 1001006;

    @ExceptionHandler
    @ResponseBody
    public JsonResult permissionErrorHandler(AuthorizationException e) {
        JsonResult result = new JsonResult();
        result.setCode(TEMP_PERMISSION_DENIED);
        result.setMsg(e.getMessage());
        if ("The current Subject is not authenticated.  Access denied.".equals(result.getMsg())) {
            result.setCode(TEMP_USER_ERROR);
        } else if (result.getMsg().startsWith("This subject is anonymous")) {
            result.setCode(TEMP_USER_ERROR);
        }
        return result;
    }

    @ExceptionHandler
    @ResponseBody
    public JsonResult accountErrorHandler(UnknownAccountException e) {
        JsonResult result = new JsonResult();
        result.setCode(TEMP_ACCOUNT_NOT_EXIST);
        result.setMsg("账号不存在");
        return result;
    }

    @ExceptionHandler
    @ResponseBody
    public JsonResult passwordErrorHandler(IncorrectCredentialsException e) {
        SessionContext.setSessionAttr(SessionContext.ADMIN_USER, null);
        SessionContext.setSessionAttr(SessionContext.ADMIN_LOGIN_PASSWORD_ERROR, e.getMessage());
        JsonResult result = new JsonResult();
        result.setCode(TEMP_PASSWORD_ERROR);
        result.setMsg("账号或密码错误");
        return result;
    }

    @ExceptionHandler
    @ResponseBody
    public JsonResult displayableExceptionHandler(DisplayableException de) {
        HttpServletRequest request = SessionContext.getRequest();
        if (request != null) {
            log.error("请求URI：" + request.getRequestURI());
        }
        JsonResult result = new JsonResult();
        String[] strings = de.getMessage().split("\\|");
        int code = Integer.parseInt(strings[0]);
        String msg = strings[1];
        result.setCode(code);
        String fuzzyMsg = fuzzyLoginErrorMsg(code);
        result.setMsg(fuzzyMsg != null ? fuzzyMsg : msg);
        log.error("接口调用不通过，原因：" + msg);
        return result;
    }
    private final static Integer[] LOGIN_ERROR = {TEMP_PASSWORD_ERROR, TEMP_USER_ERROR};
    public static String fuzzyLoginErrorMsg(int code) {
        if (Arrays.asList(LOGIN_ERROR).contains(code)) {
            return "账号或密码错误!";
        }
        return null;
    }

    @ExceptionHandler
    @ResponseBody
    public JsonResult methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        HttpServletRequest request = SessionContext.getRequest();
        if (request != null) {
            log.error("请求URI：" + request.getRequestURI());
        }
        JsonResult result = new JsonResult(TEMP_PARAMS_ERROR, "param error");
        log.error("接口调用不通过，原因：" + result.getMsg());
        return result;
    }

    @ExceptionHandler
    @ResponseBody
    public JsonResult httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e) {
        HttpServletRequest request = SessionContext.getRequest();
        if (request != null) {
            log.error("请求URI：" + request.getRequestURI());
        }
        JsonResult result = new JsonResult(TEMP_PARAMS_ERROR, "param error");
        log.error("接口调用不通过，原因：" + result.getMsg());
        return result;
    }

    @ExceptionHandler
    @ResponseBody
    public JsonResult exceptionHandler(Exception e) {
        HttpServletRequest request = SessionContext.getRequest();
        if (request != null) {
            log.error("请求URI：" + request.getRequestURI());
        }
        JsonResult result = new JsonResult();
        String message = e.getMessage();
        if (message != null && message.startsWith("com.lp.demo.common.exception.DisplayableException")) {
            String[] strings = message.substring(56).split("\\|");
            result.setCode(Integer.parseInt(strings[0]));
            result.setMsg(strings[1].substring(0, strings[1].indexOf(System.lineSeparator())));
            log.error("接口调用不通过，原因：" + result.getMsg());
            return result;
        }
        if (e.getCause() != null) {
            if (Objects.equals(e.getCause().getClass(), FlowException.class) ||
                    Objects.equals(e.getCause().getClass(), DegradeException.class)) {
                result.setCode(TEMP_SERVER_ERROR);
                result.setMsg("服务繁忙，请稍后重试");
                log.error("接口调用不通过，原因：服务繁忙");
                return result;
            }
        }
        result.setCode(TEMP_SERVER_ERROR);
        result.setMsg("后端程序报错");
        log.error("接口调用不通过，原因：" + result.getMsg(), e);
        return result;
    }
}
