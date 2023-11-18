package com.lp.demo.shardingsphere.jdbc.common.result;


public interface BaseEnum<T extends Enum<T> & BaseEnum<T>> {

    /**
     * 获取code码
     */
    Integer getCode();

    /**
     * 获取code码信息
     */
    String getMsg();

    /**
     * 根据code码获取枚举
     */
    static <T extends Enum<T> & BaseEnum<T>> T parseByCode(Class<T> clazz, Integer code) {
        for (T t : clazz.getEnumConstants()) {
            if (t.getCode().intValue() == code.intValue()) {
                return t;
            }
        }
        return null;
    }
}