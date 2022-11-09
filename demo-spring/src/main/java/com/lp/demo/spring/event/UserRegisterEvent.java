package com.lp.demo.spring.event;

import com.lp.demo.common.dto.User;
import com.lp.demo.common.dto.UserDto;
import com.lp.demo.common.util.ConsoleColorUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author lp
 * @date 2022/10/12 15:44
 * @desc 用户注册事件处理
 **/
@Component
public class UserRegisterEvent extends Event<User> {

    private static final long serialVersionUID = -2960160633165338114L;

    public UserRegisterEvent(ApplicationContext source) {
        super(source);
    }

    @Override
    public void execute(User user) {
        ConsoleColorUtil.printDefaultColor("UserRegisterEvent.execute = " + user);
    }
}
