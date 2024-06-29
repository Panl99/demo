package com.lp.demo.common.aop.aspect;

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
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
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
                                s.charAt(0) + "***" + s.charAt(s.length() - 1);
    }
}
