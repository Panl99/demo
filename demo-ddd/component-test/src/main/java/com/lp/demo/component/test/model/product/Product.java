package com.lp.demo.component.test.model.product;

import com.lp.demo.component.codegen.processor.creator.IgnoreCreator;
import com.lp.demo.component.codegen.processor.updater.IgnoreUpdater;
import com.lp.demo.component.codegen.processor.vo.GenVo;
import com.lp.demo.component.common.constants.CodeEnum;
import com.lp.demo.component.common.constants.ValidStatus;
import com.lp.demo.component.common.converter.ValidStatusConverter;
import com.lp.demo.component.common.exception.BusinessException;
import com.lp.demo.component.common.support.AbstractEntity;
import com.lp.demo.component.test.dto.CreateProductDto;
import com.sun.org.glassfish.gmbal.Description;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;

@Data
@Entity
@Table(name = "tb_product")
@ToString(callSuper = true)
@GenVo(pkgName = "com.lp.demo.component.test.vo")
public class Product extends AbstractEntity {

    @Id
    @Getter
    @Setter
    private Long id;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "type_id")
    private Long productTypeId;

    @Column(name = "price")
    @Description(value = "指导价格")
    private BigDecimal price;

    @Convert(converter = ValidStatusConverter.class)
    @Column(name = "valid_status")
    @Description(value = "上下架状态")
    @IgnoreCreator
    @IgnoreUpdater
    private ValidStatus validStatus;

//    @Column(name = "serialize_type")
//    @Description(value = "序列化类型")
//    @Convert(converter = SerializeTypeConverter.class)
//    private SerializeType serializeType;

    @Description(value = "保修时间--销售日期为准+天数")
    @Column(name = "valid_days")
    private Integer validDays;


    public void init() {
        setValidStatus(ValidStatus.VALID);
    }

    public void createProduct(CreateProductDto dto) {
        setPrice(dto.getPrice());
        setValidDays(dto.getValidDays());
        setProductName(dto.getProductName());
//        setSerializeType(dto.getSerializeType());
        setCategoryId(dto.getCategoryId());
        setProductTypeId(dto.getTypeId());
        setProductCode(dto.getProductCode());
    }

    public void invalid() {
        if (Objects.equals(ValidStatus.INVALID, getValidStatus())) {
            throw new BusinessException(CodeEnum.STATUS_HAS_INVALID);
        }
        setValidStatus(ValidStatus.INVALID);
    }

    public void valid() {
        if (Objects.equals(ValidStatus.VALID, getValidStatus())) {
            throw new BusinessException(CodeEnum.STATUS_HAS_VALID);
        }
        setValidStatus(ValidStatus.VALID);
    }

}