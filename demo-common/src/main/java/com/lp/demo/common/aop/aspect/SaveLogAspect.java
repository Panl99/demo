package com.lp.demo.common.aop.aspect;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.lp.demo.common.aop.annotation.MaskLog;
import com.lp.demo.common.aop.annotation.SaveLog;
import com.lp.demo.common.aop.service.OperationLogService;
import com.lp.demo.common.util.ConsoleColorUtil;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 使用@SaveLog添加日志
 */
@Slf4j
@Aspect
@Component
public class SaveLogAspect {

    @Autowired
    OperationLogService operationLogService;

    // 切入点
    @Pointcut("@annotation(com.lp.demo.common.aop.annotation.SaveLog)")
    public void saveLogPointcut() {
    }

    // 在切入点的方法环绕执行
    @Around("@annotation(saveLog)")
    public Object logBeforeController(JoinPoint joinPoint, SaveLog saveLog) throws Throwable {
        Object result = null;

        // 这个RequestContextHolder是Springmvc提供来获得请求的东西
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 获取远程调用信息
        String remoteAddress = request.getRemoteAddr() + ":" + request.getRemotePort();
        // 获取场景值
        String scene = saveLog.scene();
        // 掩码 saveLog
        Map<Integer, Set<String>> masks = maskMap(saveLog.masks());
        // 获取操作用户
//        Object obj = new ThreadLocal<>().get();
//        ConsoleColorUtil.printDefaultColor(String.valueOf(obj));
        Long userId = 0L;
        String operator = "lp";

        Object[] args = joinPoint.getArgs();

        // 处理入参
        String requestBody = "";
        String[] params = saveLog.params();
        int[] indexes = saveLog.paramsIdxes();
        if (ArrayUtil.isNotEmpty(params) && ArrayUtil.isNotEmpty(indexes) && params.length == indexes.length) {
            Map<String, Object> reqMap = new HashMap<>();
            for (int i = 0; i < indexes.length; i++) {
                int idx = indexes[i];
                String reqName = params[i];
                Object param = args[idx];
                Set<String> fds = masks.get(idx);
                if (param == null) {
                    reqMap.put(reqName, "");
                } else if (param instanceof ServletRequest || param instanceof ServletResponse) {
                    reqMap.put(reqName, "");
                } else if (ObjectUtil.isEmpty(masks) || !masks.containsKey(idx)) {
                    reqMap.put(reqName, param);
                } else if (fds == null || fds.isEmpty()) {
                    reqMap.put(reqName, "***");
                } else {
                    String reqStr = JSONUtil.toJsonStr(param);
                    JSONObject jsonObject = JSONUtil.parseObj(reqStr);
                    if (jsonObject.isEmpty()) {
                        reqMap.put(reqName, "");
                        continue;
                    }
                    for (String fd : fds) {
//                        jsonObject.addProperty(fd, "***");
                        jsonObject.putOpt(fd, "***");
                    }
                    reqMap.put(reqName, JSONUtil.toJsonStr(jsonObject));
                }
            }
            requestBody = JSONUtil.toJsonStr(reqMap);
        }

        try {
            MethodInvocationProceedingJoinPoint mjoinpoint = (MethodInvocationProceedingJoinPoint) joinPoint;
            result = mjoinpoint.proceed(args);
            String res = result == null ? "" : JSONUtil.toJsonStr(result);
            operationLogService.saveLogWithBackground(userId, operator, scene, requestBody, res, remoteAddress);
        } catch (Throwable throwable) {
            throw throwable;
        }
        return result;
    }

    private Map<Integer, Set<String>> maskMap(MaskLog[] masks) {
        if (masks == null || masks.length <= 0) {
            return new HashMap<>(0);
        } else {
            Map<Integer, Set<String>> map = new HashMap<>();
            for (MaskLog mask : masks) {
                Set<String> fields = map.get(mask.paramsIdx());
                if (fields == null) {
                    fields = new HashSet<>();
                    map.put(mask.paramsIdx(), fields);
                }
                String[] fds = mask.fields();
                if (fds != null && fds.length > 0) {
                    for (String fd : fds) {
                        fields.add(fd);
                    }
                }
            }
            return map;
        }
    }







//    @SaveOperationLog(
//            scene = "测试测试",
//            paramsIdxes = {0, 1},
//            params = {"param1", "param2"},
//            masks = {@MaskLog(paramsIdx = 0,
//                    fields = {"name", "id", "phone"},
//                    maskLevel = MaskLog.MaskLevelEnum.PART),
//                    @MaskLog(paramsIdx = 1,
//                            fields = {"f2"},
//                            maskLevel = MaskLog.MaskLevelEnum.PART)
//            }
//    )

    @Around("@annotation(operationLog)")
    public Object logBefore(ProceedingJoinPoint joinPoint, SaveLog operationLog) throws Throwable {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String remoteAddress = "";
        if (StringUtil.isEmpty(remoteAddress)) {
            remoteAddress = request.getRemoteAddr();
        }
        String scene = operationLog.scene();
        MaskLog[] masks = operationLog.masks();
        Object[] args = joinPoint.getArgs();
        // 处理入参
        String requestBody = "";
        String[] params = operationLog.params();
        int[] indexes = operationLog.paramsIdxes();
        if (ArrayUtil.isNotEmpty(params) && ArrayUtil.isNotEmpty(indexes) && params.length == indexes.length) {
            Map<String, Object> reqMap = new HashMap<>();
            for (int i = 0; i < indexes.length; i++) {
                int idx = indexes[i];
                String reqName = params[i];
                Object param = args[idx];

                if (param == null) {
                    reqMap.put(reqName, "");
                } else if (param instanceof ServletRequest || param instanceof ServletResponse) {
                    reqMap.put(reqName, "");
                } else if (ObjectUtil.isEmpty(masks) || Arrays.stream(masks).map(MaskLog::paramsIdx).noneMatch(k -> k == idx)) {
                    reqMap.put(reqName, param);
                } else {
                    MaskLog mask = masks[idx];
                    MaskLog.MaskLevelEnum maskLevel = mask.maskLevel();
                    if (param instanceof Map) {
                        String reqStr = JSONUtil.toJsonStr(param);
                        JSONObject jsonObject = JSONUtil.parseObj(reqStr);
                        if (jsonObject.isEmpty()) {
                            reqMap.put(reqName, "");
                            continue;
                        }

                        for (String fd : mask.fields()) {
                            String s = jsonObject.get(fd).toString();
                            jsonObject.putOpt(fd, this.mask(s, maskLevel));
                        }
                        reqMap.put(reqName, jsonObject);
                    } else {
                        reqMap.put(reqName, this.mask(param.toString(), maskLevel));
                    }

                }
            }
            requestBody = JSONUtil.toJsonStr(reqMap);
        } else if (args != null && args.length > 0) {
            JSONObject jsonObject = new JSONObject();
            for (int i = 0; i < args.length; i++) {
                jsonObject.set("参数"+ i, args[i]);
            }
            requestBody = JSONUtil.toJsonStr(jsonObject);
        }

        System.out.println("requestBody = " + requestBody);

        MethodInvocationProceedingJoinPoint mjoinpoint = (MethodInvocationProceedingJoinPoint) joinPoint;
        Object result = mjoinpoint.proceed(args);

        System.out.println("result = " + result);

        return result;
    }

    private Object mask(String s, MaskLog.MaskLevelEnum maskLevel) {
        return maskLevel == MaskLog.MaskLevelEnum.ALL ? "******" :
                s.length() > 4 ? StrUtil.hide(s, 2, s.length() - 2) :
                        s.length() <= 0 ? "***" :
                                s.length() < 3 ? s.charAt(0) + "***" :
                                        s.charAt(0) + "***" + s.charAt(s.length() - 1);
    }

    /**
     * 减少注解需要自定义很多参数
     *
     * @param joinPoint
     * @param operationLog
     * @return
     * @throws Throwable
     */
//    @Around("@annotation(operationLog)")
    public Object logBefore2(ProceedingJoinPoint joinPoint, SaveLog operationLog) throws Throwable {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String remoteAddress = "";
        if (StringUtil.isEmpty(remoteAddress)) {
            remoteAddress = request.getRemoteAddr();
        }
        String scene = operationLog.scene();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (StringUtil.isEmpty(scene)) {
//            RequiresPermissions rp = method.getAnnotation(RequiresPermissions.class); // 方法上其它带有描述的注解
//            if (rp != null && rp.value().length > 0) {
//                scene = String.join(";", rp.value());
//            } else {
                scene = method.getName();
//            }
        }

        // 处理入参
        Map<String, Object> reqMap = new HashMap<>();
        Parameter[] params = method.getParameters();
        Object[] args = joinPoint.getArgs();
        if (ArrayUtil.isNotEmpty(params) && ArrayUtil.isNotEmpty(args)) {
            MaskLog[] masks = operationLog.masks();
            for (int i = 0; i < Math.min(params.length, args.length); i++) {
                String paramName = params[i].getName();
                Object paramValue = args[i];
                if (paramValue == null) {
                    paramValue = "";
                } else {
                    if (paramValue instanceof ServletRequest || paramValue instanceof ServletResponse) {
                        paramValue = "";
                    } else if (paramValue instanceof MultipartFile) {
                        paramValue = ((MultipartFile) paramValue).getOriginalFilename();
                    }

                    if (ArrayUtil.isNotEmpty(masks) && masks.length > i) {
                        MaskLog mask = masks.length == 1 ? masks[0] : masks[i];
                        MaskLog.MaskLevelEnum maskLevel = mask.maskLevel();
                        if (paramValue instanceof CharSequence || paramValue instanceof Number) {
                            paramValue = this.mask(paramValue.toString(), maskLevel);
                        } else {
                            String reqStr = JSONUtil.toJsonStr(paramValue);
                            JSONObject jsonObject = JSONUtil.parseObj(reqStr);
                            if (jsonObject.isEmpty()) {
                                paramValue = "";
                            } else {
                                for (String fd : mask.fields()) {
                                    if (!jsonObject.containsKey(fd)) {
                                        continue;
                                    }
                                    String s = jsonObject.get(fd).toString();
                                    jsonObject.putOpt(fd, this.mask(s, maskLevel));
                                }
                                paramValue = jsonObject;
                            }
                        }
                    }
                }
                reqMap.put(paramName, paramValue);
            }
        } else if (ArrayUtil.isNotEmpty(params)) {
            for (int i = 0; i < params.length; i++) {
                reqMap.put(params[i].getName(), "");
            }
        } else if (ArrayUtil.isNotEmpty(args)) {
            for (int i = 0; i < args.length; i++) {
                reqMap.put("参数" + (i + 1), args[i]);
            }
        }

        System.out.println("reqMap = " + reqMap);

        MethodInvocationProceedingJoinPoint mjoinpoint = (MethodInvocationProceedingJoinPoint) joinPoint;
        Object result = mjoinpoint.proceed(args != null ? args : new Object[0]);

        System.out.println("result = " + result);
        return result;
    }

    @Around("@annotation(operationLog) || " +
            "(@within(operationLog) && (execution(* com.lp..controller..*(..))) && (" +
            "           @annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "           @annotation(org.springframework.web.bind.annotation.PutMapping) || " +
            "           @annotation(org.springframework.web.bind.annotation.DeleteMapping) || " +
            "           @annotation(org.springframework.web.bind.annotation.PatchMapping) ||" +
            "           @annotation(org.springframework.web.bind.annotation.RequestMapping) " +
            ")))")
    public Object operationLogAround(ProceedingJoinPoint joinPoint,
                                     SaveLog operationLog) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping != null) {
            RequestMethod[] methods = requestMapping.method();
            if (!CollectionUtil.containsAny(Arrays.asList(methods), Arrays.asList(RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE))) {
                return joinPoint.proceed();
            }
        }
        // 获取方法上的注解（优先）
        SaveLog methodAnnotation = AnnotationUtils.findAnnotation(method, SaveLog.class);
        // 获取类上的注解
        SaveLog clazzAnnotation = AnnotationUtils.findAnnotation(joinPoint.getTarget().getClass(), SaveLog.class);

        // 1.优先使用方法注解的方式
//        return this.logBefore2(joinPoint, methodAnnotation != null ? methodAnnotation : clazzAnnotation);
        // 2.使用方法注解和类注解合并的方式
        return this.logBefore2(joinPoint, this.mergeAnnotations(clazzAnnotation, methodAnnotation));
    }

    /**
     * 合并方法注解和类注解参数
     *
     * @param clazzAnnotation
     * @param methodAnnotation
     * @return
     */
    private SaveLog mergeAnnotations(SaveLog clazzAnnotation, SaveLog methodAnnotation) {
        // 1.只有方法注解
        if (methodAnnotation != null && clazzAnnotation == null) {
            return methodAnnotation;
        }
        // 2.只有类注解
        if (methodAnnotation == null && clazzAnnotation != null) {
            return clazzAnnotation;
        }
        // 3.两者都存在，使用方法注解值（仅当方法注解使用默认值时回退到类注解）
        return new SaveLog() {

            @Override
            public int[] paramsIdxes() {
                return (methodAnnotation.paramsIdxes() == null || methodAnnotation.paramsIdxes().length < 1) ? clazzAnnotation.paramsIdxes() : methodAnnotation.paramsIdxes();
            }

            @Override
            public String[] params() {
                return (methodAnnotation.params() == null || methodAnnotation.params().length < 1) ? clazzAnnotation.params() : methodAnnotation.params();
            }

            @Override
            public String scene() {
                return StringUtil.isEmpty(methodAnnotation.scene()) ? clazzAnnotation.scene() : methodAnnotation.scene();
            }

            @Override
            public MaskLog[] masks() {
                return (methodAnnotation.masks() == null || methodAnnotation.masks().length < 1) ? clazzAnnotation.masks() : methodAnnotation.masks();
            }

            @Override
            public String[] value() {
                return (methodAnnotation.value() == null || methodAnnotation.value().length < 1) ? clazzAnnotation.value() : methodAnnotation.value();
            }

            // 实现注解接口的其他必要方法...
            @Override
            public Class<? extends Annotation> annotationType() {
                return SaveLog.class;
            }
        };
    }

    /**
     * @SaveLog的使用方式：
     * 1、默认使用：@SaveLog，
     * 2、自定义参数：
     *     @SaveLog(value = xxx,
     *             scene = "新增",
     *             masks = {@MaskLog(
     *                     fields = {"name", "isDefault"},
     *                     maskLevel = MaskLog.MaskLevelEnum.PART),
     *                     @MaskLog(paramsIdx = 1,
     *                             fields = {"str"},
     *                             maskLevel = MaskLog.MaskLevelEnum.PART)
     *             }
     *     )
     * 3、对所有参数中名称为"password"和"secret"的全部打码：
     *     @SaveLog(masks =
     *              @MaskLog(fields = {"password", "secret"},
     *                     maskLevel = MaskLog.MaskLevelEnum.ALL)
     *
     *     )
     * 4、只对第二个参数中名称为"name"的部分打码：
     *     @SaveLog(masks = {
     *                  @MaskLog, // 注意：这里表示第一个参数，为必须
     *                  @MaskLog(fields = {"name"})
     *              }
     *     )
     */
}
