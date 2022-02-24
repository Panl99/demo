package com.lp.demo.common.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lp
 * @date 2022/1/21 11:33
 **/
public enum DeviceSceneTypeBaiduEnum {
    // 回家
    GO_HOME,
    // 离家
    LEAVE_HOME,
    // 睡眠
    SLEEP,
    // 早安
    MORNING,
    // 外出守护
    GUARD,
    // 明亮
    BRIGHT,
    // 全关
    CLOSE_ALL,
    // 全开
    OPEN_ALL,
    // 起夜
    WAKE_UP,
    // 自动开灯
    AUTO_TURN_ON_LIGHT,
    // 自动关灯
    AUTO_TURN_OFF_LIGHT,
    // 工作
    WORK,
    // 用餐
    DINNER,
    // 阅读
    READ,
    // 观影
    MOVIE,
    // 洗澡
    BATH,
    // 柔和
    DARK,
    // 插卡取电
    POWER_ON_THE_CARD,
    // 拔卡断电
    POWER_OFF_THE_CARD,
    // 会客
    RECEPTION,
    // 温馨
    WARM;

    DeviceSceneTypeBaiduEnum() {
    }

    public static String contains(String sceneType) {
        List<String> nameList = Arrays.stream(DeviceSceneTypeBaiduEnum.values()).map(DeviceSceneTypeBaiduEnum::name).collect(Collectors.toList());
        System.out.println("deviceSceneTypeBaiduEnumList = " + nameList);
        if (nameList.contains(sceneType.toUpperCase())) {
            return sceneType.toUpperCase();
        }
        return null;
    }
}
