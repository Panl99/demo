package com.lp.demo.common.util.mapstruct;


import com.lp.demo.common.dto.User;
import com.lp.demo.common.dto.WebLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author lp
 * @date 2021/9/18 14:57
 **/
public class MapStructTest {
    public static void main(String[] args) {

        ProductDto productDto = new ProductDto();
        productDto.setProductId(1001);
        productDto.setProductName("智能灯");
        productDto.setProductPrice("500");
        productDto.setProductStatus(Boolean.TRUE);

        Product productObj = ProductStructMapper.MAPPER.productDto2Entity(productDto);
        System.out.println("productObj = " + productObj);


        Product product = new Product();
        product.setId(2001);
        product.setName("机器人");
        product.setPrice("10000");
        product.setStatus(0);

        User user = new User();
        user.setUserId(111);
        user.setUserName("张三");
        user.setPhoneNumberList(Arrays.asList(12345, 777889));

        WebLog webLog = new WebLog();
        webLog.setUsername(user.getUserName());
        webLog.setDescription("测试333");

        ProductVo productVo = ProductStructMapper.MAPPER.productEntity2Vo(product, user, webLog);
        System.out.println("productVo = " + productVo);

        List<Product> productList = new ArrayList<>();
        productList.add(productObj);
        productList.add(product);

        List<ProductVo> productVoList = ProductStructMapper.MAPPER.productList2Vo(productList, user, webLog);
        System.out.println("productVoList = " + productVoList);


//        UserDto userDto = UserDto.initUserDto();
//
//        User user = CommonStructMapper.MAPPER.user2userDto(userDto);
//        ConsoleColorUtil.printDefaultColor(user.toString());
    }
}
