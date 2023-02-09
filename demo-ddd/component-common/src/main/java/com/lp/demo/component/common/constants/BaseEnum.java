package com.lp.demo.component.common.constants;

/**
 * 配合一个工具类EnumDictUtils可以方便的给前端返回 错误信息和错误码 的下拉常量列表。
 * @param <T>
 */
public interface BaseEnum<T extends Enum<T> & BaseEnum<T>> {

    Integer getCode();

    String getName();

    static <T extends Enum<T> & BaseEnum<T>> T parseByCode(Class<T> cls, Integer code) {
        for (T t : cls.getEnumConstants()) {
            if (t.getCode().intValue() == code.intValue()) {
                return t;
            }
        }
        return null;
    }
}