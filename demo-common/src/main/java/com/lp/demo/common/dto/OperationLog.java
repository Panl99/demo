package com.lp.demo.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

/**
 * @author lp
 * @date 2022/4/20 14:32
 * 通过拦截器实现操作日志收集
 **/
@Data
public class OperationLog {
    /**
     * id
     */
    private Long id;

    /**
     * 操作人ID
     */
    private Long userId;

    /**
     * 操作人名称
     */
    private String operator;

    /**
     * 场景
     */
    private String scene;

    /**
     * 返回参数
     */
    private String response;

    /**
     * 操作地址
     */
    @JsonIgnore
    private String remoteAddress;

    /**
     * 操作时间
     */
    private Date createTime = new Date();

    /**
     * 请求参数
     */
    private String request;
}
