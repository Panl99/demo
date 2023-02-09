package com.lp.demo.component.test.service.impl.product;

import com.lp.demo.component.test.dto.CreateProductDto;
import com.lp.demo.component.test.repository.product.ProductRepository;
import com.lp.demo.component.test.service.product.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author lp
 * @date 2023/1/15 14:28
 * @desc todo
 **/
@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void validProduct(Long id) {

    }

    @Override
    public void invalidProduct(Long id) {

    }

    @Override
    public void create(CreateProductDto dto) {

    }
}
