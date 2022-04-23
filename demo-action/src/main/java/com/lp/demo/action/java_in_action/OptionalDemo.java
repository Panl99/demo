package com.lp.demo.action.java_in_action;

import com.lp.demo.common.dto.User;
import com.lp.demo.common.exception.DisplayableException;
import com.lp.demo.common.result.ResultEnum;

import java.util.Optional;

/**
 * @author lp
 * @date 2022/4/23 9:32
 **/
public class OptionalDemo {

    public static String getUserNameOnJava8(User user) {
        return Optional.ofNullable(user)
                .map(User::getUserName)
                .orElseThrow(() -> new DisplayableException(ResultEnum.FAIL));
    }

    public static User getUserOnJava8(User user) {
        return Optional.ofNullable(user)
                .filter(u -> "zhangsan".equals(u.getUserName()))
                .orElseGet(() -> {
                    User user1 = new User();
                    user1.setUserName("zhangsan");
                    return user1;
                });
    }

    public static void main(String[] args) {
        User user = new User();

        Optional.ofNullable(user)
                .ifPresent(u -> {
                    Optional.ofNullable(getUserOnJava8(u))
                            .ifPresent(OptionalDemo::getUserNameOnJava8);
                });
    }

}
