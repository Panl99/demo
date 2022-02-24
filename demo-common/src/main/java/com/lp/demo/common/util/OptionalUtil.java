package com.lp.demo.common.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.lp.demo.common.dto.User;
import com.lp.demo.common.dto.UserDto;
import com.lp.demo.common.result.JsonResult;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author lp
 * @date 2021/11/22 15:40
 **/
public class OptionalUtil {
    /**
     * 对可能为null的对象进行调用处理
     *
     * @param source
     * @param function
     * @param <T>
     * @param <R>
     * @return defaultValue null
     * @see #mapTo(Object, Function, Object)
     */
    public static <T, R> R mapTo(T source, Function<T, R> function) {
        return Optional.ofNullable(source).map(function).orElse(null);
    }

    /**
     * 对可能为null的对象进行调用处理并返回默认值
     *
     * @param source
     * @param function
     * @param defaultValue 默认返回值
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> R mapTo(T source, Function<T, R> function, R defaultValue) {
        return Optional.ofNullable(source).map(function).orElse(defaultValue);
    }

    /**
     * list转换
     *
     * @param collection
     * @param fun
     * @param <T>
     * @param <R>
     * @return non null
     */
    public static <T, R> List<R> mapToList(Collection<T> collection, Function<T, R> fun) {
        return CollectionUtils.isNotEmpty(collection) ? collection.stream().map(fun).collect(Collectors.toList()) : Lists.newArrayList();
    }

    /**
     * set转换
     *
     * @param collection
     * @param fun
     * @param <T>
     * @param <R>
     * @return non null
     */
    public static <T, R> Set<R> mapToSet(Collection<T> collection, Function<T, R> fun) {
        return CollectionUtils.isNotEmpty(collection) ? collection.stream().map(fun).collect(Collectors.toSet()) : Sets.newHashSet();
    }

    /**
     * 转成map
     *
     * @param collection
     * @param keyFun     keep first
     * @param <T>
     * @param <K>
     * @return non null
     */
    public static <T, K> Map<K, T> mapToIdMap(Collection<T> collection, Function<T, K> keyFun) {
        return CollectionUtils.isNotEmpty(collection) ? collection.stream().collect(Collectors.toMap(keyFun, Function.identity(), (a, b) -> a)) : Maps.newHashMap();
    }

    /**
     * 转成map
     *
     * @param collection
     * @param keyFun
     * @param valFun     keep first
     * @param <T>
     * @param <K>
     * @param <V>
     * @return
     */
    public static <T, K, V> Map<K, V> mapToIdMap(Collection<T> collection, Function<T, K> keyFun, Function<T, V> valFun) {
        return CollectionUtils.isNotEmpty(collection) ? collection.stream().collect(Collectors.toMap(keyFun, valFun, (a, b) -> a)) : Maps.newHashMap();
    }

    /**
     * 按给定条件分组
     *
     * @param collection
     * @param groupKeyFun
     * @param <T>
     * @param <K>
     * @return non null
     */
    public static <T, K> Map<K, List<T>> mapToGroupMap(Collection<T> collection, Function<T, K> groupKeyFun) {
        return CollectionUtils.isNotEmpty(collection) ? collection.stream().collect(Collectors.groupingBy(groupKeyFun)) : Maps.newHashMap();
    }

    /**
     * 按给定条件分组
     *
     * @param collection
     * @param groupKeyFun
     * @param groupValFun
     * @param <T>
     * @param <K>
     * @param <V>
     * @return
     */
    public static <T, K, V> Map<K, List<V>> mapToGroupMap(Collection<T> collection, Function<T, K> groupKeyFun, Function<T, V> groupValFun) {
        return CollectionUtils.isNotEmpty(collection) ? collection.stream().collect(Collectors.groupingBy(groupKeyFun, Collectors.mapping(groupValFun, Collectors.toList()))) : Maps.newHashMap();
    }

    /**
     * 按给定条件分组(结果集排重)
     *
     * @param collection
     * @param groupKeyFun
     * @param groupValFun
     * @param <T>
     * @param <K>
     * @param <V>
     * @return
     */
    public static <T, K, V> Map<K, Set<V>> mapToGroupSetMap(Collection<T> collection, Function<T, K> groupKeyFun, Function<T, V> groupValFun) {
        return CollectionUtils.isNotEmpty(collection) ? collection.stream().collect(Collectors.groupingBy(groupKeyFun, Collectors.mapping(groupValFun, Collectors.toSet()))) : Maps.newHashMap();
    }

    /**
     * 对可能为null的对象进行消费
     *
     * @param source
     * @param consumer
     * @param <T>
     */
    public static <T> void consume(T source, Consumer<T> consumer) {
        Optional.ofNullable(source).ifPresent(consumer);
    }

    public static void main(String[] args) {
        List<UserDto> userPOList = Collections.singletonList(UserDto.initUserDto());
        //转换成以唯一标识为键的map
        Map<Integer,UserDto> userPOMap = OptionalUtil.mapToIdMap(userPOList, UserDto::getPhoneNumber);
        //转换成以指定字段分组的列表
        Map<String,List<UserDto>> userNameGroupMap = OptionalUtil.mapToGroupMap(userPOList, UserDto::getName);
        //对象转换
        List<User> userBOList = OptionalUtil.mapToList(userPOList, userDto->{
            User u = new User();
            u.setPhoneNumberList(Collections.singletonList(userDto.getPhoneNumber()));
//            u.setUserName(userDto.getName());
            //name为null时不会执行lambda，减少null的判断
            u.setUserName(OptionalUtil.mapTo(userDto.getName(), name -> name/*.split("-")[0]*/));
            return u;
        });

        ConsoleColorUtil.printDefaultColor(userPOMap.toString());
        ConsoleColorUtil.printDefaultColor(userNameGroupMap.toString());
        ConsoleColorUtil.printDefaultColor(userBOList.toString());

    }

    public class UmsMember {
        private Long id;
        // 会员等级id
        private Long memberLevelId;

        @ApiModelProperty(value = "用户名")
        private String username;

        @ApiModelProperty(value = "密码")
        private String password;

        @ApiModelProperty(value = "昵称")
        private String nickname;

        @ApiModelProperty(value = "手机号码")
        private String phone;

        @ApiModelProperty(value = "帐号启用状态:0->禁用；1->启用")
        private Integer status;

        @ApiModelProperty(value = "注册时间")
        private Date createTime;

        @ApiModelProperty(value = "头像")
        private String icon;

        @ApiModelProperty(value = "性别：0->未知；1->男；2->女")
        private Integer gender;

        @ApiModelProperty(value = "生日")
        private Date birthday;

        @ApiModelProperty(value = "所做城市")
        private String city;

        @ApiModelProperty(value = "职业")
        private String job;

        @ApiModelProperty(value = "个性签名")
        private String personalizedSignature;

        @ApiModelProperty(value = "用户来源")
        private Integer sourceType;

        @ApiModelProperty(value = "积分")
        private Integer integration;

        @ApiModelProperty(value = "成长值")
        private Integer growth;

        @ApiModelProperty(value = "剩余抽奖次数")
        private Integer luckeyCount;

        @ApiModelProperty(value = "历史积分数量")
        private Integer historyIntegration;

    }
}
