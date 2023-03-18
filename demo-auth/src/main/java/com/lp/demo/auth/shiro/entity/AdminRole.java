package com.lp.demo.auth.shiro.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AdminRole implements Serializable {
    private static final long serialVersionUID = 868103296481807342L;
    /**
     * 主键，自增长
     */
    private Long id;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 权限列表
     */
    private List<AdminPermission> permissions;
}