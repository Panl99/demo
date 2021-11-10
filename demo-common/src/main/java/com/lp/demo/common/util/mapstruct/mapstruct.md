# 背景
随着微服务和分布式应用程序迅速占领开发领域，数据完整性和安全性比以往任何时候都更加重要。在这些松散耦合的系统之间，安全的通信渠道和有限的数据传输是最重要的。大多数时候，终端用户或服务不需要访问模型中的全部数据，而只需要访问某些特定的部分。

数据传输对象(DTO)经常被用于这些应用中。DTO只是持有另一个对象中被请求的信息的对象。通常情况下，这些信息是有限的一部分。例如，在持久化层定义的实体和发往客户端的DTO之间经常会出现相互之间的转换。

MapStruct是一个开源的基于Java的代码生成器，用于创建实现Java Bean之间转换的扩展映射器。使用MapStruct，我们只需要创建接口，而该库会通过注解在编译过程中自动创建具体的映射实现，大大减少了通常需要手工编写的样板代码的数量。

**一句话总结：用于Dto、Entity、Vo之间的转换。**

[GitHub](https://github.com/mapstruct/mapstruct) <br/>
[官网](https://mapstruct.org/ "官网") <br/>
[参考指南](https://mapstruct.org/documentation/stable/reference/html/)

# 依赖
```xml
<properties>
	<mapstruct.version>1.4.2.Final</mapstruct.version>
</properties>

<dependency>
	<groupId>org.mapstruct</groupId>
	<artifactId>mapstruct</artifactId>
	<version>${mapstruct.version}</version>
</dependency>
<dependency>
	<groupId>org.mapstruct</groupId>
	<artifactId>mapstruct-jdk8</artifactId>
	<version>${mapstruct.version}</version>
</dependency>
<dependency>
	<groupId>org.mapstruct</groupId>
	<artifactId>mapstruct-processor</artifactId>
	<version>${mapstruct.version}</version>
</dependency>
```

# 使用
自定义一些测试的实体类

## 实体类
**Dto**
```java
@Data
public class ProductDto {
    private Integer productId;
    private String productName;
    private String productPrice;
    private Boolean productStatus;
}
```
**Entity**
```java
@Data
public class Product {
    private Integer id;
    private String name;
    private String price;
    private Integer status;
}

```

**Vo**
```java
@Data
public class ProductVo {
    // 产品名称
    private Integer productId;
    // 产品名称
    private String productName;
    // 产品价格
    private String productPrice;
    // 产品状态
    private Boolean productStatus;
    // 获取时间
    private Date getInfoTime;
    // 用户id
    private Integer userId;
    // 用户名
    private String userName;
}

```


## 编写映射接口
```java
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Date;
import java.util.List;

/**
 * @author lp
 * @date 2021/9/18 14:16
 **/
@Mapper //注意是mapstruct的注解，不是ibatis的！
public interface ProductStructMapper {

    ProductStructMapper MAPPER = Mappers.getMapper(ProductStructMapper.class);

    @Mappings({
            @Mapping(source = "productId", target = "id"),
            @Mapping(source = "productName", target = "name"),
            @Mapping(source = "productPrice", target = "price"),
            @Mapping(source = "productStatus", target = "status", qualifiedByName = "convertStatus")
    })
    Product productDto2Entity(ProductDto productDto);

    @Named("convertStatus")
    default int convertStatus(Boolean status) {
        return status.equals(Boolean.TRUE) ? 1 : 0;
    }

    @Mappings({
            @Mapping(source = "product.id", target = "productId"),
            @Mapping(source = "product.name", target = "productName"),
            @Mapping(source = "product.price", target = "productPrice"),
            @Mapping(target = "productStatus", ignore = true), // 忽略status字段
            @Mapping(target = "getInfoTime", expression = "java(getInfoTime(new java.util.Date()))")
    })
    ProductVo productEntity2Vo(Product product);

    List<ProductVo> productList2Entity(List<Product> productList);

    @Named("getInfoTime")
    default Date getInfoTime() {
        return new Date();
    }
}

```

## 编译后自动生成实现文件
```java
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-10-23T15:06:44+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_191 (Oracle Corporation)"
)
public class ProductStructMapperImpl implements ProductStructMapper {

    @Override
    public Product productDto2Entity(ProductDto productDto) {
        if ( productDto == null ) {
            return null;
        }

        Product product = new Product();

        product.setId( productDto.getProductId() );
        product.setName( productDto.getProductName() );
        product.setPrice( productDto.getProductPrice() );
        product.setStatus( convertStatus( productDto.getProductStatus() ) );

        return product;
    }

    @Override
    public ProductVo productEntity2Vo(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductVo productVo = new ProductVo();

        productVo.setProductId( product.getId() );
        productVo.setProductName( product.getName() );
        productVo.setProductPrice( product.getPrice() );

        productVo.setGetInfoTime( getInfoTime(new java.util.Date()) );

        return productVo;
    }

    @Override
    public List<ProductVo> productList2Vo(List<Product> productList) {
        if ( productList == null ) {
            return null;
        }

        List<ProductVo> list = new ArrayList<ProductVo>( productList.size() );
        for ( Product product : productList ) {
            list.add( productEntity2Vo( product ) );
        }

        return list;
    }
}

```

## 编写测试方法
```java

import com.lp.demo.common.dto.User;
import com.lp.demo.common.dto.UserDto;
import com.lp.demo.common.util.ConsoleColorUtil;

import java.util.ArrayList;
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

        ProductVo productVo = ProductStructMapper.MAPPER.productEntity2Vo(product);
        System.out.println("productVo = " + productVo);

        List<Product> productList = new ArrayList<>();
        productList.add(productObj);
        productList.add(product);

        List<ProductVo> productVoList = ProductStructMapper.MAPPER.productList2Vo(productList);
        System.out.println("productVoList = " + productVoList);
    }
}

```

### 执行结果
```java
productObj = Product(id=1001, name=智能灯, price=500, status=1)
productVo = ProductVo(productId=2001, productName=机器人, productPrice=10000, productStatus=null, getInfoTime=2021-10-23 16:39:48:118)
productVoList = [ProductVo(productId=1001, productName=智能灯, productPrice=500, productStatus=null, getInfoTime=2021-10-23 16:39:48:158), ProductVo(productId=2001, productName=机器人, productPrice=10000, productStatus=null, getInfoTime=2021-10-23 16:39:48:159)]


```

# 方法总结
## 数据类型不一致
````java
@Mapping(source = "productStatus", target = "status", qualifiedByName = "convertStatus")

@Named("convertStatus")
default int convertStatus(Boolean status) {
    return status.equals(Boolean.TRUE) ? 1 : 0;
}
````

## 忽略某个属性
`@Mapping(target = "productStatus", ignore = true), // 忽略status字段`

## 列表转换
```java
ProductVo productEntity2Vo(Product product);
// 直接放到实体类转换下边即可
List<ProductVo> productList2Vo(List<Product> productList);

```

## 自定义方法
通过表达式指定方法，为字段赋值:
`@Mapping(target = "getInfoTime", expression = "java(getInfoTime(new java.util.Date()))"),`


**以上是mapstruct的一些基本用法，其他场景可以自己探索下**

[https://www.jianshu.com/p/56b97843af61?utm_campaign=hugo](https://www.jianshu.com/p/56b97843af61?utm_campaign=hugo) <br/>
[https://zhuanlan.zhihu.com/p/368731266](https://zhuanlan.zhihu.com/p/368731266)