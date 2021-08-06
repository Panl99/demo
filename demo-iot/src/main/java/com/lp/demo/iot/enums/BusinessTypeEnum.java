package com.lp.demo.iot.enums;

/**
 * @author lp
 * @date 2021/5/31 12:05
 **/
public enum BusinessTypeEnum {
    // 阿里业务
    ALI("ali"),
    // 百度业务
    BAIDU("baidu"),
    // 亚马逊业务
    AWS("aws"),
    // 谷歌业务
    GOOGLE("google"),
    // 自动化（If This Then That）
    IFTTT("ifttt");

    private String name;

    BusinessTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
