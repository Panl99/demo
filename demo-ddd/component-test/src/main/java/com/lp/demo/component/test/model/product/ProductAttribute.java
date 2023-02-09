package com.lp.demo.component.test.model.product;

import com.lp.demo.component.codegen.processor.vo.GenVo;
import com.lp.demo.component.common.support.AbstractEntity;
import com.lp.demo.component.test.dto.AttrBean;
import com.sun.org.glassfish.gmbal.Description;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "tb_product_attribute")
@GenVo(pkgName = "com.lp.demo.component.test.vo")
public class ProductAttribute extends AbstractEntity {

    @Id
    @Getter
    @Setter
    private Long id;

    @Column(name = "product_id")
    @Setter
    private Long productId;

    @Setter(AccessLevel.PROTECTED)
    @Column(name = "attr_name")
    @Description(value = "属性名称")
    private String attrName;

    @Column(name = "attr_code")
    @Description(value = "属性编码")
    private String attrCode;

    @Column(name = "attr_desc")
    @Description(value = "属性描述")
    private String attrDesc;

    @Column(name = "sort_num")
    private Integer sortNum;

//    @Column(name = "web_control_type")
//    @Description(value = "前端控件类型")
//    @Convert(converter = WebControlTypeConverter.class)
//    private WebControlType controlType;

    public void createAttr(Long productId, AttrBean bean) {
        setSortNum(0);
        setProductId(productId);
//        setControlType(bean.getControlType());
        setAttrCode(bean.getAttrCode());
        setAttrDesc(bean.getAttrDesc());
        setAttrName(bean.getAttrName());
    }
}