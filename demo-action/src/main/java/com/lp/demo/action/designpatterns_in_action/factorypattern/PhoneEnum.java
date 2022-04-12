package com.lp.demo.action.designpatterns_in_action.factorypattern;

import java.util.function.Supplier;

/**
 * @create 2021/1/27 21:58
 * @auther outman
 **/
public enum PhoneEnum {
    HUAWEI(HuaWei::new),
    IPHONE(IPhone::new),
    XIAOMI(XiaoMi::new);

    private final Supplier<Phone> phone;

    PhoneEnum(Supplier<Phone> phone) {
        this.phone = phone;
    }

    public Supplier<Phone> getPhone() {
        return phone;
    }
}
