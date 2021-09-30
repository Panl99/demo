package com.lp.demo.common.util.mapstruct;

import com.lp.demo.common.dto.User;
import com.lp.demo.common.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.temporal.ChronoUnit;

/**
 * @author lp
 * @date 2021/9/18 14:16
 **/
@Mapper
public interface CommonMapper {

    CommonMapper MAPPER = Mappers.getMapper(CommonMapper.class);

    @Mappings({
//            @Mapping(source = "name", target = "userName"),
            @Mapping(target = "userName", expression = "java(concatName(userDto.getAge(), java.time.temporal.ChronoUnit.DAYS))"),
            @Mapping(source = "phoneNumber", target = "phoneNumber"),
            @Mapping(source = "age", target = "userId"),
            @Mapping(source = "name", target = "isStudent", qualifiedByName = "convertStatus")
    })
    User user2userDto(UserDto userDto);

    @Named("convertStatus")
    default Boolean convertStatus(String name) {
        if ("zhangsan".equals(name)) {
            return true;
        }
        return false;
    }

    @Named("concatName")
    default String concatName(Integer value, ChronoUnit unit) {
        return value + "#" + unit;
    }
}
