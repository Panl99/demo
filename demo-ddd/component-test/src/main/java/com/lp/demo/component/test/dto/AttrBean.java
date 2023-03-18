package com.lp.demo.component.test.dto;

import com.sun.org.glassfish.gmbal.Description;
import lombok.Data;


@Data
public class AttrBean {

    private Long productId;

    @Description(value = "属性名称")
    private String attrName;

    @Description(value = "属性编码")
    private String attrCode;

    @Description(value = "属性描述")
    private String attrDesc;

    private Integer sortNum;

//    @Description(value = "前端控件类型")
//    @Convert(converter = WebControlTypeConverter.class)
//    private WebControlType controlType;

}