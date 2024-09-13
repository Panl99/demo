package com.lp.demo.common.aop.aspect;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.lp.demo.common.aop.annotation.DecryptField;
import com.lp.demo.common.aop.annotation.MaskLog;
import com.lp.demo.common.config.SecretConfig;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

/**
 * @author lp
 */
@Slf4j
@Aspect
@Component
public class DecryptAspect {

    @Autowired
    SecretConfig secretConfig;

    @Pointcut(value = "@annotation(com.lp.demo.common.aop.annotation.NeedDecrypt)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object o = proceedingJoinPoint.proceed();
        if (o instanceof List) {
            for (Object tmp : ((List) o)) {
                this.deepProcess(tmp);
            }
        } else {
            this.deepProcess(o);
        }
        return o;
    }


    public void deepProcess(Object obj) throws IllegalAccessException {
        if (Objects.isNull(obj)) {
            return;
        }
        Field[] fields = ReflectUtil.getFields(obj.getClass());
        for (Field field : fields) {
            if (field.isAnnotationPresent(DecryptField.class)) {
                field.setAccessible(true);
                Object valObj = field.get(obj);
                if (valObj != null) {
                    String value = this.decrypt(valObj.toString());
                    DecryptField annotation = field.getAnnotation(DecryptField.class);
                    if (annotation.isMask()) {
                        value = this.mask(value, annotation.maskLevel());
                    }
                    field.set(obj, value);
                }
            }
        }
    }

    private String decrypt(String value) {
        String decryptKey = secretConfig.getEncryptKey();
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue(), decryptKey.getBytes()).getEncoded();
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);
        return aes.decryptStr(value);
    }

    private String mask(String s, MaskLog.MaskLevelEnum maskLevel) {
        return maskLevel == MaskLog.MaskLevelEnum.ALL ? "***" :
                s.length() > 4 ? StrUtil.hide(s, 2, s.length() - 2) :
                        s.length() <= 0 ? "***" :
                                s.charAt(0) + "***" + s.charAt(s.length() - 1);
    }

}