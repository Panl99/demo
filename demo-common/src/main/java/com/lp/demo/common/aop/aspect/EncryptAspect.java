package com.lp.demo.common.aop.aspect;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.lp.demo.common.aop.annotation.EncryptField;
import com.lp.demo.common.config.SecretConfig;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Objects;

/**
 * @author lp
 */
@Slf4j
@Aspect
@Component
public class EncryptAspect {

    @Autowired
    SecretConfig secretConfig;

    @Pointcut(value = "@annotation(com.lp.demo.common.aop.annotation.NeedEncrypt)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取入参
        Object[] args = joinPoint.getArgs();
        // 获取方法参数注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (Objects.isNull(arg) ||
                    arg instanceof Number ||
                    arg instanceof Boolean) {
                continue;
            }

            if (arg instanceof Collection) {
                for (Object tmp : ((Collection) arg)) {
                    this.processField(tmp);
                }
            } else {
                this.processField(arg);
            }

            Annotation[] annotations = parameterAnnotations[i];
            for (Annotation annotation : annotations) {
                if (annotation instanceof EncryptField) {
                    String encrypt = this.encrypt(arg.toString());
                    args[i] = encrypt;
                }
            }
        }

        return joinPoint.proceed(args);
    }


    private void processField(Object obj) throws IllegalAccessException {
        if (Objects.isNull(obj)) {
            return;
        }
        Field[] fields = ReflectUtil.getFields(obj.getClass());
        for (Field field : fields) {
            if (field.isAnnotationPresent(EncryptField.class)) {
                field.setAccessible(true);
                Object valObj = field.get(obj);
                if (valObj != null) {
                    String value = this.encrypt(valObj.toString());
                    field.set(obj, value);
                }
            }
        }
    }


//    private void processMethodParam(ProceedingJoinPoint joinPoint, Object[] args) {
//        // 获取方法参数注解
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Method method = signature.getMethod();
//        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
//        for (int i = 0; i < parameterAnnotations.length; i++) {
//            Object arg = args[i];
//            Annotation[] annotations = parameterAnnotations[i];
//            if (Objects.isNull(arg) || annotations.length == 0) {
//                continue;
//            }
//            for (Annotation annotation : annotations) {
//                if (annotation instanceof EncryptField) {
//                    String encrypt = this.encrypt(arg.toString());
//                    args[i] = encrypt;
//                }
//            }
//        }
//    }

    private String encrypt(String value) {
        String encryptKey = secretConfig.getEncryptKey();
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue(), encryptKey.getBytes()).getEncoded();
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);
        return aes.encryptBase64(value);
    }

}