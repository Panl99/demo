package com.lp.demo.common.util.mapstruct;

import com.lp.demo.common.dto.User;
import com.lp.demo.common.dto.UserDto;
import com.lp.demo.common.util.ConsoleColorUtil;

/**
 * @author lp
 * @date 2021/9/18 14:57
 **/
public class MapStructTest {
    public static void main(String[] args) {
        UserDto userDto = UserDto.initUserDto();

        User user = CommonMapper.MAPPER.user2userDto(userDto);
        ConsoleColorUtil.printDefaultColor(user.toString());
    }
}
