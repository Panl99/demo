package com.lp.demo.common.util;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.lp.demo.common.dto.User;
import com.lp.demo.common.dto.UserDto;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.UUID;
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
     *
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
     *
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
     *
     * @param obj
     * @return
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * 复制指定的属性
     *
     * @param src     源对象
     * @param trg     目标对象
     * @param objects 指定属性（不论是否为空，全复制）
     */
    public static void copySpecifiedProperties(Object src, Object trg, Object... objects) {
        String[] ignoreProperties = Arrays.stream(getAllFieldName(src)).filter(obj -> !ArrayUtils.contains(objects, obj)).toArray(String[]::new);
        BeanUtils.copyProperties(src, trg, ignoreProperties);
    }

    /**
     * 复制指定的非空属性
     *
     * @param src     源对象
     * @param trg     目标对象
     * @param objects 指定属性（只复制非空字段）
     */
    public static void copyNotEmptySpecifiedProperties(Object src, Object trg, Object... objects) {
        String[] ignoreProperties = Arrays.stream(getAllFieldName(src)).filter(obj -> {
            Object fieldValue = getFieldValueByName(src, obj);
            return !ArrayUtils.contains(objects, obj) || isEmpty(fieldValue);
        }).toArray(String[]::new);
        BeanUtils.copyProperties(src, trg, ignoreProperties);
    }

    /**
     * 获取全部字段名
     *
     * @param obj 类对象
     * @return
     */
    public static String[] getAllFieldName(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        return Arrays.stream(fields).map(Field::getName).toArray(String[]::new);
    }

    /**
     * 根据字段名获取值
     *
     * @param obj       类对象
     * @param fieldName 字段名
     * @return
     */
    public static Object getFieldValueByName(Object obj, String fieldName) {
        Field field = null;
        try {
            field = obj.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        if (field == null) {
            return "";
        }
        return getFieldValueByName(obj, field);
    }

    private static Object getFieldValueByName(Object obj, Field field) {
        field.setAccessible(true);
        Object fieldVal = "";
        try {
            fieldVal = field.get(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return fieldVal;
    }

    /**
     * 获取所有字段值
     *
     * @param obj 类对象
     * @return
     */
    public static Map getAllFieldValueByName(Object obj) {
        String[] allFieldName = getAllFieldName(obj);
        Map<String, Object> fieldMap = new HashMap<>(allFieldName.length);
        List<String> allFields = new ArrayList<>(Arrays.asList(allFieldName));
        allFields.stream().forEach(field -> fieldMap.put(field, getFieldValueByName(obj, field)));
        return fieldMap;
    }

    /**
     * 获取指定字段的值
     * @param obj 类对象
     * @param fields 指定字段
     * @return
     */
    public static Map getSpecifiedFieldValueByName(Object obj, String... fields) {
        Map<String, Object> fieldMap = new HashMap<>(fields.length);
        Arrays.stream(fields).forEach(field -> fieldMap.put(field, getFieldValueByName(obj, field)));
        System.out.println(fieldMap);
        return fieldMap;
    }

    /**
     * 列表对象去重
     * 针对 具体类对象
     */
    private static List<Object> distinctObj(List<UserDto> objs) {
        return objs.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(
                                Comparator.comparing(o -> o.getName() + o.getAge() + o.getHobby()))), ArrayList::new));

    }

    /**
     * 列表对象去重
     * @param objs 类对象
     * @param fields 指定唯一字段
     * @return
     */
    public static List<Object> distinctObjAbstract(List<Object> objs, String... fields) {
        return objs.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(
                                Comparator.comparing(o -> getSpecifiedFieldValueByName(o, fields).toString()))), ArrayList::new));

    }

    public static void main(String[] args) {
        UserDto user = UserDto.initUserDto();

        // 获取全部字段名
//        System.out.println(Arrays.stream(getAllFieldName(user)).collect(Collectors.toList()));

        // 获取全部字段值
        Map allFieldValue = getAllFieldValueByName(user);
//        System.out.println(allFieldValue);

        // 对象列表去重
        UserDto userDto = new UserDto();
        userDto.setName("zhangsan");
        userDto.setAge(19);
        userDto.setAddress("广东");
        userDto.setDate(new Date());
        List<String> hobby = new ArrayList<>();
        hobby.add("篮球");
        hobby.add("羽毛球");
        hobby.add("爬山");
        userDto.setHobby(hobby);
        userDto.setPhoneNumber(123456789);
        Map<String, String> school = new HashMap<>();
        school.put("小学", "庞各庄一小");
        school.put("中学", "庞各庄一中");
        school.put("大学", "庞各庄大一");
        userDto.setSchool(school);
        UserDto u2 = userDto;

        UserDto userDto2 = new UserDto();
        userDto2.setName("zhangsan");
        userDto2.setAge(18);
        userDto2.setAddress("广东");
        userDto2.setDate(new Date());
        List<String> hobby2 = new ArrayList<>();
        hobby2.add("篮球");
        hobby2.add("羽毛球");
        hobby2.add("看电影");
        userDto2.setHobby(hobby2);
        userDto2.setPhoneNumber(123456789);
        Map<String, String> school2 = new HashMap<>();
        school2.put("小学", "庞各庄一小");
        school2.put("中学", "庞各庄一中");
        school2.put("大学", "庞各庄大一");
        userDto2.setSchool(school2);
        UserDto u3 = userDto2;
        List<Object> userList = new ArrayList<>();
        userList.add(user);
        userList.add(u2);
        userList.add(u3);
        List<Object> resultList = distinctObjAbstract(userList, "name","age","hobby");
        System.out.println(resultList);


        listRemove(hobby);

    }

    /**
     * 泛型Test
     * @param data 不同类型的List
     * @param type 具体类型
     * @param action
     * @param clientId
     * @param <T>
     */
    public <T extends List<T>> void pushMessage(List<T> data, Class<T> type, String action, String clientId) {
        if (CollectionUtil.isNotEmpty(data)) {
            String msg = JSON.toJSONString(getMsg(data, action));
            for (T t : data) {
                T obj = toObject(t, type);
                Object placeId = ObjectUtil.getFieldValueByName(obj, "placeId");
                ConsoleColorUtil.printDefaultColor(clientId + msg + placeId);
            }
        }
    }

    public static <T extends List<T>> T toObject(T data, Class<T> type) {
        if (CollectionUtil.isEmpty(data)) {
            return null;
        }
        T t = null;
        try {
            t = JSONUtil.toBean(data.toString(), type);
        } catch (Exception e) {
            ConsoleColorUtil.printDefaultColor("to object fail."+ e);
        }
        return t;
    }

    /**
     * 判断 object instanceof List<MyType> 的方式
     * https://stackoverflow.com/questions/10108122/how-to-instanceof-listmytype
     *
     * 1. if (object instanceof List && ((List) object).stream().noneMatch((o -> !(o instanceof MyType)))) {}
     * 检查全部列表项而不是取第一个检查：防止列表类型是List<Object>
     */
    private UserDto getMsg(Object o, String action) {
        User user = new User();
        if (o instanceof List && ((List) o).stream().allMatch((obj -> (obj instanceof User)))) {
            user.setUserId(10001);
            user.setUserName("10001");
//            user.setPhoneNumber(100010001);
        }
        List<String> list = new ArrayList<>();
        list.add(user.toString());

        UserDto appMsg = new UserDto();
        appMsg.setName(UUID.randomUUID().toString());
        appMsg.setAddress(action);
        appMsg.setHobby(list);
        return appMsg;
    }

    /**
     * 泛型在编译时会擦除数据类型
     * 可以通过包装器来保存List所包含的类型
     */
    static class GenericList<T> extends ArrayList<T> {
        private Class<T> genericType;

        public GenericList(Class<T> c) {
            this.genericType = c;
        }

        public Class<T> getGenericType() {
            return genericType;
        }
    }


    /**
     * list remove
     */
    public static List<String> listRemove(List<String> paramList) {
        List<String> list = new ArrayList<>(5);
        list.add("篮球");
        list.add("羽毛球");
        list.add("看电影");
        list.add("看书");
        list.add("打游戏");

//        Iterator<String> iterator = paramList.iterator();
//        while (iterator.hasNext()) {
//            String param = iterator.next();
//            if (!list.contains(param)) {
//                iterator.remove();
//            }
//        }
         paramList.removeIf(param -> !list.contains(param));


        for (String s : list) {
//            if (iterator.hasNext()) {
//                String param = iterator.next();
//                if (s.equals(param)) {
//                    iterator.remove();
//                }
//            }
            paramList.remove(s);
        }


        System.out.println("list = " + list);
        System.out.println("listRemove = " + paramList);

        List<Integer> lengthList = paramList.stream().map(String::length).collect(Collectors.toList());
        System.out.println("lengthList = " + lengthList);

        return paramList;
    }

}
