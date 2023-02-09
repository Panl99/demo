package com.lp.demo.component.test.service.product;

import com.lp.demo.component.service.AbstractService;
import com.lp.demo.component.test.dto.CreateProductDto;

/**
 * @author lp
 * @date 2023/1/15 14:27
 * @desc
 **/
public interface ProductService /*extends AbstractService*/ {

    void validProduct(Long id);

    void invalidProduct(Long id);

    void create(CreateProductDto dto);
}
