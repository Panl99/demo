package com.lp.demo.langchain4j.example.enums;


import java.util.Objects;

public interface BaseEnum<T extends Enum<T> & BaseEnum<T>> {

    /**
     * code
     */
    String getCode();

    /**
     * name
     */
    String getName();

    /**
     * 根据code码获取枚举
     *
     * @param cls  enum class
     * @param code enum code
     * @return get enum
     */
    static <T extends Enum<T> & BaseEnum<T>> T getByCode(Class<T> cls, String code) {
        for (T t : cls.getEnumConstants()) {
            if (Objects.equals(t.getCode(), code)) {
                return t;
            }
        }
        return null;
    }

}