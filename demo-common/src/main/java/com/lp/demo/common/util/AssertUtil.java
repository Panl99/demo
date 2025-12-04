package com.lp.demo.common.util;

import cn.hutool.core.collection.CollectionUtil;
import com.lp.demo.common.exception.DisplayableException;
import com.lp.demo.common.result.BaseEnum;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class AssertUtil {

    /**
     * 异常创建位置正确：异常是在调用代码中通过 Supplier 创建的，而不是在工具类内部创建的
     * 堆栈信息准确：异常的堆栈跟踪会从调用代码开始，而不是从工具类内部开始
     * 延迟创建异常：只有在supplier条件满足时才会调用exceptionSupplier的get()方法创建异常对象，避免不必要的性能开销
     * 使用灵活：可以直接创建自定义的异常消息和错误码
     * <p>
     * 在Java中，使用Supplier<DisplayableException>作为参数确实可以保证对象的延迟创建。这是因为Supplier是一个函数式接口，其get()方法只有在被调用时才会执行并创建对象
     * 具体到AssertUtil.ifThen()方法的实现，当第一个参数（条件判断lambda表达式）返回true时，才会调用第二个参数（Supplier）的get()方法来创建异常对象。这种设计避免了不必要的对象创建，只有在真正需要抛出异常时才会实例化异常对象
     * 相比之下，如果直接传递new DisplayableException()作为参数，无论条件是否满足，都会立即创建异常对象，这可能导致不必要的性能开销
     * 因此，使用Supplier是实现延迟创建的有效方式。
     *
     * @param supplier          条件表达式
     * @param exceptionSupplier 异常表达式
     */
    public static void ifThen(Supplier<Boolean> supplier, Supplier<DisplayableException> exceptionSupplier) {
        Optional.ofNullable(supplier.get())
                .filter(BooleanUtils::isFalse)
                .orElseThrow(exceptionSupplier);
    }

    public static void ifThen(Boolean bool, Supplier<DisplayableException> exceptionSupplier) {
        Optional.ofNullable(bool)
                .filter(BooleanUtils::isFalse)
                .orElseThrow(exceptionSupplier);
    }

    public static void ifBlankStrThrowEx(String value, Supplier<DisplayableException> exceptionSupplier) {
        ifThen(StringUtils.isBlank(value), exceptionSupplier);
    }

    public static void ifNotBlankStrThrowEx(String value, Supplier<DisplayableException> exceptionSupplier) {
        ifThen(StringUtils.isNotBlank(value), exceptionSupplier);
    }

    public static void ifNullObjThrowEx(Object value, Supplier<DisplayableException> exceptionSupplier) {
        ifThen(Objects.isNull(value), exceptionSupplier);
    }

    public static void ifNotNullObjThrowEx(Object value, Supplier<DisplayableException> exceptionSupplier) {
        ifThen(!Objects.isNull(value), exceptionSupplier);
    }

    public static void ifEmptyCollectionThrowEx(Collection<?> value, Supplier<DisplayableException> exceptionSupplier) {
        ifThen(CollectionUtil.isEmpty(value), exceptionSupplier);
    }

    public static void ifNotEmptyCollectionThrowEx(Collection<?> value, Supplier<DisplayableException> exceptionSupplier) {
        ifThen(CollectionUtil.isNotEmpty(value), exceptionSupplier);
    }

    /**
     * ------------------------------------------------不建议使用下方方式，建议使用上边方式------------------------------------------------
     * <p>
     * 原因：异常在工具方法中创建，导致在全局异常处理中打印堆栈日志时(默认打印第一行)不能定位到异常在业务代码中的调用位置
     */

    public static void ifThen(Supplier<Boolean> supplier, BaseEnum baseEnum) {
        Optional.ofNullable(supplier.get())
                .filter(BooleanUtils::isFalse)
                .orElseThrow(() -> new DisplayableException(baseEnum));
    }

    public static void ifThen(Supplier<Boolean> supplier, BaseEnum baseEnum, String... args) {
        Optional.ofNullable(supplier.get())
                .filter(BooleanUtils::isFalse)
                .orElseThrow(() -> new DisplayableException(baseEnum, args));
    }

    public static void ifThen(Supplier<Boolean> supplier, Integer code, String msg) {
        Optional.ofNullable(supplier.get())
                .filter(BooleanUtils::isFalse)
                .orElseThrow(() -> new DisplayableException(code, msg));
    }

    public static void ifThen(Supplier<Boolean> supplier, BaseEnum baseEnum, String msg) {
        Optional.ofNullable(supplier.get())
                .filter(BooleanUtils::isFalse)
                .orElseThrow(() -> new DisplayableException(baseEnum.getCode(), msg));
    }

    public static void ifThen(Boolean bool, BaseEnum baseEnum) {
        Optional.ofNullable(bool)
                .filter(BooleanUtils::isFalse)
                .orElseThrow(() -> new DisplayableException(baseEnum));
    }

    public static void ifThen(Boolean bool, BaseEnum baseEnum, String msg) {
        Optional.ofNullable(bool)
                .filter(BooleanUtils::isFalse)
                .orElseThrow(() -> new DisplayableException(baseEnum.getCode(), msg));
    }

    public static void ifThen(Boolean bool, BaseEnum baseEnum, String... args) {
        Optional.ofNullable(bool)
                .filter(BooleanUtils::isFalse)
                .orElseThrow(() -> new DisplayableException(baseEnum, args));
    }

    public static void ifBlankStrThrowEx(String value, BaseEnum baseEnum) {
        ifThen(() -> StringUtils.isBlank(value), baseEnum);
    }

    public static void ifBlankStrThrowEx(String value, BaseEnum baseEnum, String... args) {
        ifThen(() -> StringUtils.isBlank(value), baseEnum, args);
    }

    public static void ifBlankStrThrowEx(String value, Integer code, String msg) {
        ifThen(() -> StringUtils.isBlank(value), code, msg);
    }

    public static void ifBlankStrThrowEx(String value, BaseEnum baseEnum, String msg) {
        ifThen(StringUtils.isBlank(value), baseEnum, msg);
    }

    public static void ifNotBlankStrThrowEx(String value, BaseEnum baseEnum) {
        ifThen(() -> StringUtils.isNotBlank(value), baseEnum);
    }

    public static void ifNotBlankStrThrowEx(String value, BaseEnum baseEnum, String... args) {
        ifThen(() -> StringUtils.isNotBlank(value), baseEnum, args);
    }

    public static void ifNotBlankStrThrowEx(String value, Integer code, String msg) {
        ifThen(() -> StringUtils.isNotBlank(value), code, msg);
    }

    public static void ifNotBlankStrThrowEx(String value, BaseEnum baseEnum, String msg) {
        ifThen(StringUtils.isNotBlank(value), baseEnum, msg);
    }

    public static void ifNullObjThrowEx(Object value, BaseEnum baseEnum) {
        ifThen(() -> Objects.isNull(value), baseEnum);
    }

    public static void ifNullObjThrowEx(Object value, BaseEnum baseEnum, String... args) {
        ifThen(() -> Objects.isNull(value), baseEnum, args);
    }

    public static void ifNullObjThrowEx(Object value, Integer code, String msg) {
        ifThen(() -> Objects.isNull(value), code, msg);
    }

    public static void ifNullObjThrowEx(Object value, BaseEnum baseEnum, String msg) {
        ifThen(Objects.isNull(value), baseEnum, msg);
    }

    public static void ifNotNullObjThrowEx(Object value, BaseEnum baseEnum) {
        ifThen(() -> !Objects.isNull(value), baseEnum);
    }

    public static void ifNotNullObjThrowEx(Object value, BaseEnum baseEnum, String... args) {
        ifThen(() -> !Objects.isNull(value), baseEnum, args);
    }

    public static void ifNotNullObjThrowEx(Object value, Integer code, String msg) {
        ifThen(() -> !Objects.isNull(value), code, msg);
    }

    public static void ifNotNullObjThrowEx(Object value, BaseEnum baseEnum, String msg) {
        ifThen(!Objects.isNull(value), baseEnum, msg);
    }

    public static void ifEmptyCollectionThrowEx(Collection<?> value, BaseEnum baseEnum) {
        ifThen(() -> CollectionUtil.isEmpty(value), baseEnum);
    }

    public static void ifEmptyCollectionThrowEx(Collection<?> value, BaseEnum baseEnum, String... args) {
        ifThen(() -> CollectionUtil.isEmpty(value), baseEnum, args);
    }

    public static void ifEmptyCollectionThrowEx(Collection<?> value, Integer code, String msg) {
        ifThen(() -> CollectionUtil.isEmpty(value), code, msg);
    }

    public static void ifEmptyCollectionThrowEx(Collection<?> value, BaseEnum baseEnum, String msg) {
        ifThen(CollectionUtil.isEmpty(value), baseEnum, msg);
    }

    public static void ifNotEmptyCollectionThrowEx(Collection<?> value, BaseEnum baseEnum) {
        ifThen(() -> CollectionUtil.isNotEmpty(value), baseEnum);
    }

    public static void ifNotEmptyCollectionThrowEx(Collection<?> value, BaseEnum baseEnum, String... args) {
        ifThen(() -> CollectionUtil.isNotEmpty(value), baseEnum, args);
    }

    public static void ifNotEmptyCollectionThrowEx(Collection<?> value, Integer code, String msg) {
        ifThen(() -> CollectionUtil.isNotEmpty(value), code, msg);
    }

    public static void ifNotEmptyCollectionThrowEx(Collection<?> value, BaseEnum baseEnum, String msg) {
        ifThen(CollectionUtil.isNotEmpty(value), baseEnum, msg);
    }

}