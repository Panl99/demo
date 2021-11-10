package com.lp.demo.common.util.mapstruct;

import com.lp.demo.common.dto.WebLog;
import lombok.Data;

/**
 * @author lp
 * @date 2021/10/23 14:12
 **/
@Data
public class Product {
    // 产品id
    private Integer id;
    // 产品名称
    private String name;
    // 产品价格
    private String price;
    // 产品状态
    private Integer status;
    // web日志
    private WebLog webLog;
}
