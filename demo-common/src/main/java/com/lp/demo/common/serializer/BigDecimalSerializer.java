package com.lp.demo.common.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author lp
 */
public class BigDecimalSerializer extends JsonSerializer<BigDecimal> {

    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        // 去除末尾0
        gen.writeNumber(value.stripTrailingZeros());
    }

    /**
     * 值
     */
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal value;
}