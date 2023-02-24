package com.lp.demo.auth.shiro.entity;

import lombok.Data;
import java.io.Serializable;

@Data
public class AdminPermission implements Serializable {

    private static final long serialVersionUID = 8787281656583763452L;
    /**
     * 主键，自增长
     */
    private Long id;
    /**
     * 权限名称
     */
    private String permissionName;
    /**
     * 类型，1表示接口权限，2表示路由权限
     */
    private Integer type;
    /**
     * 接口url或路由url
     */
    private String url;
}