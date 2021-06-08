package com.lp.demo.common.util;


import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * @author lp
 * @date 2021/5/20 23:07
 * @description
 **/
public class ObjectUtil {

    /**
     * 对象全部为空
     *
     * @param objects
     * @return 全部是空对象时，返回true；否则返回false
     */
    public static boolean isAllEmpty(Object... objects) {
        for (Object o : objects) {
            if (!isEmpty(o)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 存在空对象
     * @param objects
     * @return 存在空对象时，返回true；否则返回false
     */
    public static boolean isAnyEmpty(Object... objects) {
        for (Object o : objects) {
            if (isEmpty(o)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 对象是否为空
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        } else if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        } else if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        } else if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        } else {
            return obj instanceof Map ? ((Map) obj).isEmpty() : false;
        }
    }

    /**
     * 对象是否非空
     * @param obj
     * @return
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * 复制指定的属性
     * @param src 源对象
     * @param trg 目标对象
     * @param objects 指定属性（不论是否为空，全复制）
     */
    public static void copySpecifiedProperties(Object src, Object trg, Object... objects) {
        String[] ignoreProperties = Arrays.stream(getAllFieldName(src)).filter(obj -> !ArrayUtils.contains(objects, obj)).toArray(String[]::new);
        BeanUtils.copyProperties(src, trg, ignoreProperties);
    }

    /**
     * 复制指定的非空属性
     * @param src 源对象
     * @param trg 目标对象
     * @param objects 指定属性（只复制非空字段）
     */
    public static void copyNotEmptySpecifiedProperties(Object src, Object trg, Object... objects) {
        String[] ignoreProperties = Arrays.stream(getAllFieldName(src)).filter(obj -> {
            Object fieldValue = null;
            try {
                fieldValue = getFieldValueByName(src, obj);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            return !ArrayUtils.contains(objects, obj) || isEmpty(fieldValue);
        }).toArray(String[]::new);
        BeanUtils.copyProperties(src, trg, ignoreProperties);
    }

    /**
     * 获取全部字段名
     * @param obj
     * @return
     */
    public static String[] getAllFieldName(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        return Arrays.stream(fields).map(Field::getName).toArray(String[]::new);
    }

    public static Object getFieldValueByName(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }

}
