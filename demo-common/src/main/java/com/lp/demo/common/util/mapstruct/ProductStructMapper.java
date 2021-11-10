package com.lp.demo.common.util.mapstruct;

import com.lp.demo.common.dto.User;
import com.lp.demo.common.dto.WebLog;
import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lp
 * @date 2021/9/18 14:16
 **/
@Mapper //注意是mapstruct的注解，不是ibatis的！
public interface ProductStructMapper {

    ProductStructMapper MAPPER = Mappers.getMapper(ProductStructMapper.class);

    /**
     * 对象转换
     * 1. 字段名称不一致转换
     * 2. 字段类型不一致转换
     * 3. 校验字段非空时才赋值
     * 4. 列表对象转换
     *
     * ProductDto -> Product
     * List<ProductDto> -> List<Product>
     */
    @Mappings({
            @Mapping(source = "productId", target = "id"), // 1.
            @Mapping(source = "productName", target = "name"),
            @Mapping(source = "productPrice", target = "price"/*, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS*/), // 3. 单个字段限制，全部可使用↓
            @Mapping(source = "productStatus", target = "status", qualifiedByName = "convertStatus") // 2.
    })
    @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS) // 3.校验非空
    Product productDto2Entity(ProductDto productDto);

    List<Product> productDtoList2Entity(List<ProductDto> productDto); // 4.

    @Named("convertStatus")
    default int convertStatus(Boolean status) {
        return status.equals(Boolean.TRUE) ? 1 : 0;
    }

    /**
     *
     * 1. 忽略某字段
     * 2. TODO 多属性源列表转换 List<ProductVo> = List<Product> + User + WebLog
     * 3. TODO List<> Map<> 等操作
     *
     * Product -> ProductVo
     * List<Product> -> List<ProductVo>
     */
    @Mappings({
            @Mapping(source = "product.id", target = "productId"),
            @Mapping(source = "product.name", target = "productName"),
            @Mapping(source = "product.price", target = "productPrice"),
            @Mapping(target = "productStatus", ignore = true), // 1.忽略status字段
            @Mapping(target = "getInfoTime", expression = "java(getInfoTime(new java.util.Date()))"),
            @Mapping(source = "user.userId", target = "userId"),
            @Mapping(source = "user.userName", target = "userName"),
            @Mapping(source = "webLog.description", target = "description")
    })
    ProductVo productEntity2Vo(Product product, User user, WebLog webLog);
// TODO 多属性源列表转换 当前不生效....
    List<ProductVo> productList2Vo(List<Product> productList, @Context User user, @Context WebLog webLog);

    @Named("getInfoTime")
    default String getInfoTime(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(date);
    }


    //

}
