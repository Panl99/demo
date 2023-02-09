package com.lp.demo.component.common.util;

import com.lp.demo.component.common.constants.BaseEnum;
import com.lp.demo.component.common.model.EnumDict;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class EnumDictUtils {

    private EnumDictUtils() {
    }

    public static <T extends Enum<T> & BaseEnum<T>> List<EnumDict> getEnumDicts(Class<T> cls) {
        return EnumSet.allOf(cls).stream()
                .map(et -> new EnumDict(et.getCode(), et.getName()))
                .collect(Collectors.toList());
    }

}