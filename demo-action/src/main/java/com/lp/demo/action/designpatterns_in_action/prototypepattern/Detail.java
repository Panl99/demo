package com.lp.demo.action.designpatterns_in_action.prototypepattern;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author lp
 * @date 2022/7/23 23:29
 * @desc 3.引用对象
 **/
@Data
@AllArgsConstructor
public class Detail implements Cloneable {
    /**
     * 手机号
     */
    private String phone;
    /**
     * 爱好
     */
    private List<String> hobby;
    /**
     * 地址
     */
    private String address;

    /**
     * 需要重写clone()
     * @return
     * @throws CloneNotSupportedException
     */
    @Override
    public Detail clone() throws CloneNotSupportedException {
        return (Detail) super.clone();
    }
}
