package com.lp.demo.action.spring_in_action.debounce.keyparser;

import com.lp.demo.action.spring_in_action.debounce.annotation.Debounce;
import org.aspectj.lang.JoinPoint;

/**
 * @author lp
 * @date 2022/7/25 15:50
 * @desc 防抖key解析器
 **/
public interface DebounceKeyParser {

    String getKey(JoinPoint joinPoint, Debounce debounce);
}
