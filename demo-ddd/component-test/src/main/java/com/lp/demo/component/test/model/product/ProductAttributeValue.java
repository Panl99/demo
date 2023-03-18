package com.lp.demo.component.test.model.product;

import com.lp.demo.component.codegen.processor.vo.GenVo;
import com.lp.demo.component.common.support.AbstractEntity;
import com.sun.org.glassfish.gmbal.Description;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "tb_product_attribute_value")
@Data
@Entity
@GenVo(pkgName = "com.lp.demo.component.test.vo")
public class ProductAttributeValue extends AbstractEntity {

    @Column(name = "product_attr_id")
    private Long productAttrId;

    @Description(value = "属性的值")
    private String attrValue;

    public ProductAttributeValue(Long productAttrId, String attrValue) {
        this.productAttrId = productAttrId;
        this.attrValue = attrValue;
    }

}