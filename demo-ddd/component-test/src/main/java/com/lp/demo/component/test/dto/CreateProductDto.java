package com.lp.demo.component.test.dto;

import com.lp.demo.component.codegen.processor.creator.IgnoreCreator;
import com.lp.demo.component.codegen.processor.updater.IgnoreUpdater;
import com.lp.demo.component.codegen.processor.vo.GenVo;
import com.lp.demo.component.common.constants.CodeEnum;
import com.lp.demo.component.common.constants.ValidStatus;
import com.lp.demo.component.common.converter.ValidStatusConverter;
import com.lp.demo.component.common.exception.BusinessException;
import com.lp.demo.component.common.support.AbstractEntity;
import com.sun.org.glassfish.gmbal.Description;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Data
public class CreateProductDto implements Serializable {

    private static final long serialVersionUID = -4959229982446499207L;

    private String productName;

    private String productCode;

    private Long categoryId;

    private Long typeId;

    @Description(value = "指导价格")
    private BigDecimal price;

    @Description(value = "上下架状态")
    private ValidStatus validStatus;

//    @Column(name = "serialize_type")
//    @Description(value = "序列化类型")
//    @Convert(converter = SerializeTypeConverter.class)
//    private SerializeType serializeType;

    @Description(value = "保修时间--销售日期为准+天数")
    private Integer validDays;

}