package com.lp.demo.business.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
public class BusinessDTO implements Serializable {

    private static final long serialVersionUID = 2016925894213908566L;

    private String userId;

    private String commodityCode;

    private String name;

    private Integer count;

    private BigDecimal amount;

}
