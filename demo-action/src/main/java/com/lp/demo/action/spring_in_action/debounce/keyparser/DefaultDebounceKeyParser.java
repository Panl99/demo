package com.lp.demo.action.spring_in_action.debounce.keyparser;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.lp.demo.action.spring_in_action.debounce.annotation.Debounce;
import org.aspectj.lang.JoinPoint;


/**
 * @author lp
 * @desc 默认防抖Key解析器：MD5(方法名 + 方法参数)
 */
public class DefaultDebounceKeyParser implements DebounceKeyParser {

    @Override
    public String getKey(JoinPoint joinPoint, Debounce debounce) {
        String methodName = joinPoint.getSignature().toString();
        String args = StrUtil.join(",", joinPoint.getArgs());
        return SecureUtil.md5(methodName + args);
    }
}
