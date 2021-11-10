package com.lp.demo.common.util.mapstruct;

import lombok.Data;

/**
 * @author lp
 * @date 2021/10/23 14:12
 **/
@Data
public class ProductDto {
    // 产品id
    private Integer productId;
    // 产品名
    private String productName;
    // 产品价格
    private String productPrice;
    // 产品状态
    private Boolean productStatus;
}
