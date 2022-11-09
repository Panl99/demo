package com.lp.demo.spring.event;

import com.lp.demo.common.dto.User;
import com.lp.demo.common.dto.UserDto;
import com.lp.demo.common.util.mapstruct.CommonStructMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author lp
 * @date 2022/10/13 15:09
 * @desc
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
class UserInfoEventServiceTest {

    @Autowired
    private EventPublisher eventPublisher;
    @Autowired
    private UserInfoEvent userInfoEvent;
    @Autowired
    private UserRegisterEvent userRegisterEvent;

    @Test
    void publishUserInfoEvent() {
        UserDto userDto = UserDto.initUserDto();
        userInfoEvent.setData(userDto);
        eventPublisher.publishEvent(userInfoEvent);
    }

    @Test
    void publishUserRegisterEvent() {
        User user = CommonStructMapper.MAPPER.user2userDto(UserDto.initUserDto());
        userRegisterEvent.setData(user);
        eventPublisher.publishEvent(userRegisterEvent);
    }
}