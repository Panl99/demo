package com.lp.demo.storage.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 商品信息
 */
@Data
public class CommodityDTO implements Serializable {

    private static final long serialVersionUID = -4351096748951967151L;

    private Integer id;

    private String commodityCode;

    private String name;

    private Integer count;

}
