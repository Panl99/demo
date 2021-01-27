package com.outman.democommon.designpatterns.factorypattern;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @create 2021/1/25 22:52
 * @auther outman
 **/
public class Factory {
    /**
     * 1
     */
//    public static Phone createPhone(String name) {
//        switch (name) {
//            case "huawei":
//                return new HuaWei();
//            case "iphone":
//                return new IPhone();
//            case "xiaomi":
//                return new XiaoMi();
//            default:
//                throw new RuntimeException("no such phone " + name);
//        }
//    }

    /**
     * 优化2
     */
    final static Map<String, Supplier<Phone>> map = new HashMap<>();
    static {
        map.put("huawei", HuaWei::new);
        map.put("iphone", IPhone::new);
        map.put("xiaomi", XiaoMi::new);
    }

    public static Phone createPhone(String name) {
        Supplier<Phone> p = map.get(name);
        if (p != null) {
            return p.get();
        } else {
            throw new IllegalArgumentException("no such phone " + name);
        }
    }

    /**
     * 优化3
     */
    public static Phone createPhone(PhoneEnum name) {
        Supplier<Phone> p = name.getPhone();
        if (p != null) {
            return p.get();
        } else {
            throw new IllegalArgumentException("no such phone " + name);
        }
    }

}
