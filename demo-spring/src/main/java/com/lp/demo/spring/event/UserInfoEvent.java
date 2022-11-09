package com.lp.demo.spring.event;

import com.lp.demo.common.dto.UserDto;
import com.lp.demo.common.util.ConsoleColorUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author lp
 * @date 2022/10/12 15:44
 * @desc 用户信息事件处理
 **/
@Component
public class UserInfoEvent extends Event<UserDto> {

    private static final long serialVersionUID = -2960160633165338114L;

    public UserInfoEvent(ApplicationContext source) {
        super(source);
    }

    @Override
    public void execute(UserDto userDto) {
        ConsoleColorUtil.printDefaultColor("UserInfoEvent.execute = " + userDto);
    }
}
