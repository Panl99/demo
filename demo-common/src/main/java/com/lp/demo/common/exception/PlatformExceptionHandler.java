//package com.lp.demo.common.exception;
//
//import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
//import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
//import com.lp.demo.common.result.ResultEnum;
//import com.lp.demo.common.result.JsonResult;
//import com.lp.demo.common.util.AssertUtil;
//import com.lp.demo.common.util.IpUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.dubbo.config.annotation.Reference;
//import org.apache.dubbo.rpc.RpcException;
//import org.slf4j.MDC;
//import org.springframework.beans.TypeMismatchException;
//import org.springframework.core.MethodParameter;
//import org.springframework.dao.DuplicateKeyException;
//import org.springframework.http.converter.HttpMessageNotReadableException;
//import org.springframework.util.CollectionUtils;
//import org.springframework.validation.BindException;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.FieldError;
//import org.springframework.web.HttpRequestMethodNotSupportedException;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.MissingMatrixVariableException;
//import org.springframework.web.bind.MissingPathVariableException;
//import org.springframework.web.bind.MissingRequestCookieException;
//import org.springframework.web.bind.MissingRequestHeaderException;
//import org.springframework.web.bind.MissingServletRequestParameterException;
//import org.springframework.web.bind.ServletRequestBindingException;
//import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
//import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
//import org.apache.shiro.authc.IncorrectCredentialsException;
//import org.apache.shiro.authc.UnknownAccountException;
//import org.apache.shiro.authz.AuthorizationException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.validation.ConstraintViolation;
//import javax.validation.ConstraintViolationException;
//import javax.validation.Path;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.List;
//import java.util.Objects;
//import java.util.Set;
//
//@ControllerAdvice
//@Slf4j
//public class PlatformExceptionHandler {
//
//    @ExceptionHandler
//    @ResponseBody
//    public JsonResult permissionErrorHandler(AuthorizationException e) {
//        JsonResult result = new JsonResult();
//        result.setCode(ResultEnum.PERMISSION_DENIED.getCode());
//        result.setMsg(e.getMessage());
//        if ("The current Subject is not authenticated.  Access denied.".equals(result.getMsg())) {
//            result.setCode(ResultEnum.USER_ERROR.getCode());
//        } else if (result.getMsg().startsWith("This subject is anonymous")) {
//            result.setCode(ResultEnum.USER_ERROR.getCode());
//        }
//        return result;
//    }
//
//    @ExceptionHandler
//    @ResponseBody
//    public JsonResult accountErrorHandler(UnknownAccountException e) {
//        JsonResult result = new JsonResult();
//        result.setCode(ResultEnum.ACCOUNT_NOT_EXIST.getCode());
//        result.setMsg("账号或密码错误!");
//        return result;
//    }
//
//    @ExceptionHandler
//    @ResponseBody
//    public JsonResult passwordErrorHandler(IncorrectCredentialsException e) {
//        JsonResult result = new JsonResult();
//        Userinfo userinfo = (Userinfo) SessionContext.getSessionAttr(SessionContext.PLATFORM_USERINFO);
//        // 添加登录日志
//        userLoginLogService.addAsync(userinfo.getId(), IpUtil.getClientIp(), false, userinfo.getCid(), null);
//
//        long time;
//        Date now = new Date();
//        Date lockTime;
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        //检查用户是否锁定
//        switch (userinfo.getLockGrade()) {
//            case 1:
//                //判断当前时间是否过了锁定时间
//                time = userinfo.getLockStartTime().getTime() + Userinfo.LOCK_TIME_10_MIN - now.getTime();
//                if (time < 0) {
//                    break;
//                }
//                result.setCode(ResultEnum.LOCK.getCode());
//                lockTime = new Date(userinfo.getLockStartTime().getTime() + Userinfo.LOCK_TIME_10_MIN);
//                result.setMsg("该账号已锁定，" + sdf.format(lockTime) + "后可再次登录");
//                SessionContext.setSessionAttr(SessionContext.PLATFORM_USERINFO, null);
//                return result;
//            case 2:
//                time = userinfo.getLockStartTime().getTime() + Userinfo.LOCK_TIME_30_MIN - now.getTime();
//                if (time < 0) {
//                    break;
//                }
//                result.setCode(ResultEnum.LOCK.getCode());
//                lockTime = new Date(userinfo.getLockStartTime().getTime() + Userinfo.LOCK_TIME_30_MIN);
//                result.setMsg("该账号已锁定，" + sdf.format(lockTime) + "后可再次登录");
//                SessionContext.setSessionAttr(SessionContext.PLATFORM_USERINFO, null);
//                return result;
//            case 3:
//                time = userinfo.getLockStartTime().getTime() + Userinfo.LOCK_TIME_60_MIN - now.getTime();
//                if (time < 0) {
//                    break;
//                }
//                result.setCode(ResultEnum.LOCK.getCode());
//                lockTime = new Date(userinfo.getLockStartTime().getTime() + Userinfo.LOCK_TIME_60_MIN);
//                result.setMsg("该账号已锁定，" + sdf.format(lockTime) + "后可再次登录");
//                SessionContext.setSessionAttr(SessionContext.PLATFORM_USERINFO, null);
//                return result;
//            case 4:
//                time = userinfo.getLockStartTime().getTime() + Userinfo.LOCK_TIME_24_HOUR - now.getTime();
//                if (time < 0) {
//                    break;
//                }
//                result.setCode(ResultEnum.LOCK.getCode());
//                lockTime = new Date(userinfo.getLockStartTime().getTime() + Userinfo.LOCK_TIME_24_HOUR);
//                result.setMsg("该账号已锁定，" + sdf.format(lockTime) + "后可再次登录");
//                SessionContext.setSessionAttr(SessionContext.PLATFORM_USERINFO, null);
//                return result;
//            case 5:
//                result.setCode(ResultEnum.LOCK_FOREVER.getCode());
//                result.setMsg("账号被永久锁定");
//                SessionContext.setSessionAttr(SessionContext.PLATFORM_USERINFO, null);
//                return result;
//            default:
//                break;
//        }
//        userinfo.setErrorNumber(userinfo.getErrorNumber() + 1);
//        // 超过3次密码错误需要短信验证码辅助登录
//        if (userinfo.getErrorNumber() >= 3) {
//            SessionContext.setSessionAttr(SessionContext.getSessionKey(PLATFORM_USER_LOGIN_PASSWORD_ERROR, userinfo.getId().toString()), e.getMessage());
//        }
//        if (userinfo.getErrorNumber() == 5) {
//            userinfo.setLockGrade(userinfo.getLockGrade() + 1);
//            userinfo.setErrorNumber(0);
//            userinfo.setLockStartTime(now);
//            userinfoService.updateLock(userinfo.getId(), userinfo.getLockGrade(), userinfo.getErrorNumber(), userinfo.getLockStartTime());
//            SessionContext.setSessionAttr(SessionContext.PLATFORM_USERINFO, userinfo);
//            result.setCode(ResultEnum.LOCK.getCode());
//            switch (userinfo.getLockGrade()) {
//                case 1:
//                    result.setCode(ResultEnum.LOCK.getCode());
//                    lockTime = new Date(userinfo.getLockStartTime().getTime() + Userinfo.LOCK_TIME_10_MIN);
//                    result.setMsg("该账号已锁定，" + sdf.format(lockTime) + "后可再次登录");
//                    SessionContext.setSessionAttr(SessionContext.PLATFORM_USERINFO, null);
//                    return result;
//                case 2:
//                    result.setCode(ResultEnum.LOCK.getCode());
//                    lockTime = new Date(userinfo.getLockStartTime().getTime() + Userinfo.LOCK_TIME_30_MIN);
//                    result.setMsg("该账号已锁定，" + sdf.format(lockTime) + "后可再次登录");
//                    SessionContext.setSessionAttr(SessionContext.PLATFORM_USERINFO, null);
//                    return result;
//                case 3:
//                    result.setCode(ResultEnum.LOCK.getCode());
//                    lockTime = new Date(userinfo.getLockStartTime().getTime() + Userinfo.LOCK_TIME_60_MIN);
//                    result.setMsg("该账号已锁定，" + sdf.format(lockTime) + "后可再次登录");
//                    SessionContext.setSessionAttr(SessionContext.PLATFORM_USERINFO, null);
//                    return result;
//                case 4:
//                    result.setCode(ResultEnum.LOCK.getCode());
//                    lockTime = new Date(userinfo.getLockStartTime().getTime() + Userinfo.LOCK_TIME_24_HOUR);
//                    result.setMsg("该账号已锁定，" + sdf.format(lockTime) + "后可再次登录");
//                    SessionContext.setSessionAttr(SessionContext.PLATFORM_USERINFO, null);
//                    return result;
//                case 5:
//                    result.setCode(ResultEnum.LOCK_FOREVER.getCode());
//                    result.setMsg("账号被永久锁定");
//                    SessionContext.setSessionAttr(SessionContext.PLATFORM_USERINFO, null);
//                    return result;
//                default:
//                    break;
//            }
//        }
//        userinfoService.updateLock(userinfo.getId(), userinfo.getLockGrade(), userinfo.getErrorNumber(), userinfo.getLockStartTime());
//        SessionContext.setSessionAttr(SessionContext.PLATFORM_USERINFO, null);
//        result.setCode(ResultEnum.PASSWORD_ERROR.getCode());
//        result.setMsg("账号或密码错误，再输入错误" + (5 - userinfo.getErrorNumber()) + "次将锁定账号");
//        return result;
//    }
//
//    @ExceptionHandler
//    @ResponseBody
//    public JsonResult displayableExceptionHandler(DisplayableException de) {
//        HttpServletRequest request = SessionContext.getRequest();
//        if (request != null) {
//            log.error("请求URI：" + request.getRequestURI());
//        }
//        JsonResult result = new JsonResult();
//        String[] strings = de.getMessage().split("\\|");
//        int code = Integer.parseInt(strings[0]);
//        String msg = strings[1];
//        result.setCode(code);
//        String fuzzyMsg = ResultEnum.fuzzyLoginErrorMsg(code);
//        result.setMsg(fuzzyMsg != null ? fuzzyMsg : msg);
//        log.error("接口调用不通过，DisplayableException，原因：{}，{}", msg, de.getStackTrace()[0]);
//
//        MDC.remove(REQUEST_ID);
//        return result;
//    }
//
//
//    private static final String PACKAGE_PREFIX = "com.lp.demo";
//    private static final List<String> SKIP_CLASS_NAMES = Arrays.asList(
//            AssertUtil.class.getName()
//    );
//
//    /**
//     * 从堆栈跟踪中查找第一个业务调用错误信息
//     */
//    public static StackTraceElement getRelevantStackTrace(Exception e) {
//        StackTraceElement[] stackTrace = e.getStackTrace();
//        if (stackTrace == null || stackTrace.length == 0) {
//            return null;
//        }
//        for (StackTraceElement element : stackTrace) {
//            String className = element.getClassName();
//            if (className.startsWith(PACKAGE_PREFIX) && !SKIP_CLASS_NAMES.contains(className)) {
//                return element;
//            }
//        }
//        return stackTrace[0];
//    }
//
//    private static final String MESSAGE_TEMPLATE_PREFIX = "{javax.validation.constraints";
//    @ExceptionHandler(ConstraintViolationException.class)
//    @ResponseBody
//    public JsonResult<Void> constraintViolationExceptionHandler(ConstraintViolationException e) {
//        HttpServletRequest request = SessionContext.getRequest();
//        if (request != null) {
//            log.error("请求URI：{} {}", request.getMethod(), request.getRequestURI());
//        }
//
//        String msg = e.getMessage();
//        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
//        if (!CollectionUtils.isEmpty(constraintViolations)) {
//            List<String> errMsgs = new ArrayList<>(constraintViolations.size());
//            for (ConstraintViolation<?> constraintViolation : constraintViolations) {
//                String errMsg = "";
//                String messageTemplate = constraintViolation.getMessageTemplate();
//                String message = constraintViolation.getMessage();
//                if (messageTemplate.startsWith(MESSAGE_TEMPLATE_PREFIX)) {
//                    Path propertyPath = constraintViolation.getPropertyPath();
//                    int index = 0;
//                    for (Path.Node node : propertyPath) {
//                        if (index == 1) {
//                            errMsg = node.getName() + message;
//                        }
//                        index++;
//                    }
//                    if (index != 2) {
//                        errMsg = propertyPath + message;
//                    }
//                } else {
//                    errMsg = message;
//                }
//                errMsgs.add(errMsg);
//            }
//            msg = String.join(";", errMsgs);
//        }
//
//        log.error("接口调用不通过，ConstraintViolationException，原因：{}", msg);
//        return JsonResult.fail(PARAMS_ERROR, msg);
//    }
//
//    @ExceptionHandler(BindException.class)
//    @ResponseBody
//    public JsonResult<Void> bindExceptionHandler(BindException e) {
//        HttpServletRequest request = SessionContext.getRequest();
//        if (request != null) {
//            log.error("请求URI：{} {}", request.getMethod(), request.getRequestURI());
//        }
//
//        String msg = e.getMessage();
//        BindingResult bindingResult = e.getBindingResult();
//        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
//        if (!CollectionUtils.isEmpty(fieldErrors)) {
//            List<String> errMsgs = new ArrayList<>(fieldErrors.size());
//            for (FieldError fieldError : fieldErrors) {
//                String errMsg = "";
//
//                String errorCode = fieldError.getCode();
//                String field = fieldError.getField();
//                if (TypeMismatchException.ERROR_CODE.equalsIgnoreCase(errorCode)) {
//                    errMsg = "参数类型错误";
//                } else {
//                    errMsg = "参数错误";
//                }
//                errMsgs.add(field + errMsg);
//            }
//            msg = String.join(";", errMsgs);
//        }
//
//        log.error("接口调用不通过，BindException，原因：{}", msg);
//        return JsonResult.fail(PARAMS_ERROR, msg);
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseBody
//    public JsonResult methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
//        HttpServletRequest request = SessionContext.getRequest();
//        if (request != null) {
//            log.error("请求URI：{} {}", request.getMethod(), request.getRequestURI());
//        }
//
//        String msg = e.getMessage();
//        BindingResult bindingResult = e.getBindingResult();
//        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
//        if (!CollectionUtils.isEmpty(fieldErrors)) {
//            List<String> errMsgs = new ArrayList<>(fieldErrors.size());
//            for (FieldError fieldError : fieldErrors) {
//                String errMsg = fieldError.getDefaultMessage();
//
//                try {
//                    // 解包为 Hibernate Validator 的 ConstraintViolation
//                    ConstraintViolation<?> unwrap = fieldError.unwrap(ConstraintViolation.class);
//                    // 获取原始消息模板
//                    String messageTemplate = unwrap.getMessageTemplate();
//                    String field = fieldError.getField();
//                    if (messageTemplate.startsWith(MESSAGE_TEMPLATE_PREFIX)) {
//                        errMsg = field + errMsg;
//                    }
//                } catch (Exception ex) {
//                    log.error("get error message error! e: ", ex);
//                }
//                errMsgs.add(errMsg);
//            }
//            msg = String.join(";", errMsgs);
//        }
//
//        log.error("接口调用不通过，MethodArgumentNotValidException，原因：{}", msg);
//        return JsonResult.fail(PARAMS_ERROR, msg);
//    }
//
//
//    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
//    @ResponseBody
//    public JsonResult methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException e) {
//        HttpServletRequest request = SessionContext.getRequest();
//        if (request != null) {
//            log.error("请求URI：{} {}", request.getMethod(), request.getRequestURI());
//        }
//
//        MethodParameter mp = e.getParameter();
//        Class<?> c = mp.getParameterType();
//        String targetTypeName = c.getSimpleName();
//        String msg = e.getName() + "参数类型错误";
//        String errMsg = msg + "，目标类型为(" + targetTypeName + ")类型，输入类型为(" + c.getTypeName() + ")";
//
//        log.error("接口调用不通过，MethodArgumentTypeMismatchException，原因：{}", errMsg);
//        return JsonResult.fail(PARAMS_ERROR, msg);
//    }
//
//    @ExceptionHandler(ServletRequestBindingException.class)
//    @ResponseBody
//    public JsonResult servletRequestBindingExceptionHandler(ServletRequestBindingException e) {
//        HttpServletRequest request = SessionContext.getRequest();
//        if (request != null) {
//            log.error("请求URI：{} {}", request.getMethod(), request.getRequestURI());
//        }
//
//        String msg = "参数%s缺失或错误!";
//        if (e instanceof MissingServletRequestParameterException) {
//            MissingServletRequestParameterException missingServletRequestParameterException = (MissingServletRequestParameterException) e;
//            msg = String.format(msg, missingServletRequestParameterException.getParameterName());
//        } else if (e instanceof MissingMatrixVariableException) {
//            MissingMatrixVariableException missingMatrixVariableException = (MissingMatrixVariableException) e;
//            msg = "矩阵变量" + String.format(msg, missingMatrixVariableException.getVariableName());
//        } else if (e instanceof MissingPathVariableException) {
//            MissingPathVariableException missingPathVariableException = (MissingPathVariableException) e;
//            msg = "路径变量" + String.format(msg, missingPathVariableException.getVariableName());
//        } else if (e instanceof MissingRequestCookieException) {
//            MissingRequestCookieException missingRequestCookieException = (MissingRequestCookieException) e;
//            msg = "Cookie" + String.format(msg, missingRequestCookieException.getCookieName());
//        } else if (e instanceof MissingRequestHeaderException) {
//            MissingRequestHeaderException missingRequestHeaderException = (MissingRequestHeaderException) e;
//            msg = "请求头" + String.format(msg, missingRequestHeaderException.getHeaderName());
//        } else if (e instanceof UnsatisfiedServletRequestParameterException) {
//            UnsatisfiedServletRequestParameterException unsatisfiedServletRequestParameterException = (UnsatisfiedServletRequestParameterException) e;
//            msg = "请求参数不满足条件!";
//        } else {
//            msg = msg.replace("%s", "");
//        }
//
//        log.error("接口调用不通过，ServletRequestBindingException，原因：{}, {}", msg, e.getMessage());
//        return JsonResult.fail(PARAMS_ERROR, msg);
//    }
//
//    @ExceptionHandler(DuplicateKeyException.class)
//    @ResponseBody
//    public JsonResult duplicateKeyExceptionHandler(DuplicateKeyException e) {
//        HttpServletRequest request = SessionContext.getRequest();
//        if (request != null) {
//            log.error("请求URI：{} {}", request.getMethod(), request.getRequestURI());
//        }
//
//        log.error("接口调用不通过，DuplicateKeyException，原因：{}", e.getMessage());
//        return JsonResult.fail(PARAMS_ERROR, "重复数据, 请联系管理员处理!");
//    }
//
//    @ExceptionHandler(RpcException.class)
//    @ResponseBody
//    public JsonResult rpcExceptionHandler(RpcException e) {
//        HttpServletRequest request = SessionContext.getRequest();
//        if (request != null) {
//            log.error("请求URI：{} {}", request.getMethod(), request.getRequestURI());
//        }
//
//        log.error("接口调用不通过，RpcException，原因：{}", e.getMessage());
//        return JsonResult.fail(ResultEnum.SERVER_ERROR, "服务器开小差了, 请稍后重试!");
//    }
//
//    @ExceptionHandler
//    @ResponseBody
//    public JsonResult httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException e) {
//        HttpServletRequest request = SessionContext.getRequest();
//        if (request != null) {
//            log.error("请求URI：" + request.getRequestURI());
//        }
//        JsonResult result = new JsonResult(ResultEnum.PARAMS_ERROR.getCode(), e.getMessage());
//        log.error("接口调用不通过，HttpRequestMethodNotSupportedException，原因：{}", result.getMsg());
//        return result;
//    }
//
//    @ExceptionHandler
//    @ResponseBody
//    public JsonResult httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e) {
//        HttpServletRequest request = SessionContext.getRequest();
//        if (request != null) {
//            log.error("请求URI：" + request.getRequestURI());
//        }
//
//        log.error("接口调用不通过，HttpMessageNotReadableException，原因：{}", e.getMessage());
//        return JsonResult.fail(ResultEnum.PARAMS_ERROR, "参数错误");
//    }
//
//    @ExceptionHandler
//    @ResponseBody
//    public JsonResult exceptionHandler(Exception e) {
//        HttpServletRequest request = SessionContext.getRequest();
//        if (request != null) {
//            log.error("请求URI：" + request.getRequestURI());
//        }
//        JsonResult result = new JsonResult();
//        String message = e.getMessage();
//        if (message != null && message.startsWith(DisplayableException.class.getName())) {
//            String[] strings = message.substring(56).split("\\|");
//            result.setCode(Integer.parseInt(strings[0]));
//            result.setMsg(strings[1].substring(0, strings[1].indexOf(System.lineSeparator())));
//            log.error("接口调用不通过，Exception，原因：{}", result.getMsg());
//            return result;
//        }
//        if (e.getCause() != null) {
//            if (Objects.equals(e.getCause().getClass(), FlowException.class) ||
//                    Objects.equals(e.getCause().getClass(), DegradeException.class)) {
//                log.error("接口调用不通过，Exception，原因：服务繁忙");
//                return JsonResult.fail(ResultEnum.SERVER_ERROR, "服务繁忙，请稍后重试");
//            }
//        }
//        log.error("接口调用不通过，Exception，原因：", e);
//
//        MDC.remove(REQUEST_ID);
//        return JsonResult.fail(ResultEnum.SERVER_ERROR, "服务器错误");
//    }
//}
