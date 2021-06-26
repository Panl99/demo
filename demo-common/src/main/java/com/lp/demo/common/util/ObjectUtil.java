package com.lp.demo.common.util;


import com.lp.demo.common.dto.UserDto;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
     * @param obj 类对象
     * @return
     */
    public static String[] getAllFieldName(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        return Arrays.stream(fields).map(Field::getName).toArray(String[]::new);
    }

    /**
     * 根据字段名获取值
     * @param obj 类对象
     * @param fieldName 字段名
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static Object getFieldValueByName(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = obj.getClass().getDeclaredField(fieldName);
        return getFieldValueByName(obj, field);
    }

    private static Object getFieldValueByName(Object obj, Field field) throws IllegalAccessException {
        field.setAccessible(true);
        return field.get(obj);
    }

    /**
     * 获取所有字段值
     * @param obj 类对象
     * @return
     */
    public static Map getAllFieldValueByName(Object obj) {
        String[] allFieldName = getAllFieldName(obj);
        Map<String, Object> fieldMap = new HashMap<>(allFieldName.length);
        List<String> allFields = new ArrayList<>(Arrays.asList(allFieldName));
        allFields.stream().forEach(field -> {
            Object fieldVal = "";
            try {
                fieldVal = getFieldValueByName(obj, field);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

            fieldMap.put(field, fieldVal);
        });
        return fieldMap;
    }

    public static void main(String[] args) {
        UserDto user = UserDto.initUserDto();

        // 获取全部字段名
        System.out.println(Arrays.stream(getAllFieldName(user)).collect(Collectors.toList()));

        // 获取全部字段值
        Map allFieldValue = getAllFieldValueByName(user);
        System.out.println(allFieldValue);
    }

}
