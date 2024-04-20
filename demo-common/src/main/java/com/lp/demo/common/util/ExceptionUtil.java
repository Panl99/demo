package com.lp.demo.common.util;

import com.lp.demo.common.exception.DisplayableException;
import com.lp.demo.common.result.BaseEnum;
import com.lp.demo.common.result.ResultEnum;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;


import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class ExceptionUtil {

    public static void ifThen(Supplier<Boolean> supplier, BaseEnum errorCode) {
        Optional.ofNullable(supplier.get())
                .filter(BooleanUtils::isFalse)
                .orElseThrow(() -> new DisplayableException(errorCode));
    }

    public static void ifBlankThrowException(String value, BaseEnum errorCode) {
        ifThen(() -> StringUtils.isBlank(value), errorCode);
    }

    public static void ifNullThrowException(Object value, BaseEnum errorCode) {
        ifThen(() -> Objects.isNull(value), errorCode);
    }

    public static void main(String[] args) {
        ifNullThrowException(null, ResultEnum.FAIL);

        ifBlankThrowException("", ResultEnum.FAIL);
    }
}