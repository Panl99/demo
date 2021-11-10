package com.lp.demo.common.util.mapstruct;

import com.lp.demo.common.dto.WebLog;
import lombok.Data;

import java.util.List;

/**
 * @author lp
 * @date 2021/10/23 14:12
 **/
@Data
public class ProductVo {
    // 产品名称
    private Integer productId;
    // 产品名称
    private String productName;
    // 产品价格
    private String productPrice;
    // 产品状态
    private Boolean productStatus;
    // 获取时间
    private String getInfoTime;
    // 用户id
    private Integer userId;
    // 用户名
    private String userName;
    // 电话列表
    private List<Integer> phoneNumberList;
    // 操作描述
    private String description;
}
