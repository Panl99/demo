package com.lp.demo.common.serializer;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lp
 * @date 2025/12/4 12:00
 * @desc
 **/
@Component
public class JacksonSerializer {


    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
//        return builder -> {
//            builder.serializerByType(Long.class, ToStringSerializer.instance);
//            builder.serializerByType(Long.TYPE, ToStringSerializer.instance);
//            builder.serializerByType(BigInteger.class, ToStringSerializer.instance);
//            builder.serializerByType(BigDecimal.class, new BigDecimalSerializer());
//        };

        Map<Class<?>, JsonSerializer<?>> serializers = new HashMap<>();
        serializers.put(Long.class, ToStringSerializer.instance);
        serializers.put(Long.TYPE, ToStringSerializer.instance);
        serializers.put(BigInteger.class, ToStringSerializer.instance);
        serializers.put(BigDecimal.class, new BigDecimalSerializer());

        return builder -> builder.serializersByType(serializers);
    }

}
