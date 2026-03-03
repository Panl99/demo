package com.lp.demo.common.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.lp.demo.common.aop.annotation.DecimalFormat;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class FlexibleDecimalSerializer extends JsonSerializer<BigDecimal> implements ContextualSerializer {
    private int scale = -1;
    private boolean stripTrailingZeros = true;
    private RoundingMode roundingMode = RoundingMode.HALF_UP;

    public FlexibleDecimalSerializer() {
    }

    private FlexibleDecimalSerializer(int scale, boolean stripTrailingZeros, RoundingMode roundingMode) {
        this.scale = scale;
        this.stripTrailingZeros = stripTrailingZeros;
        this.roundingMode = roundingMode;
    }

    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }
        BigDecimal result = value;
        if (scale >= 0) {
            result = result.setScale(scale, roundingMode);
        }
        if (stripTrailingZeros) {
            result = result.stripTrailingZeros();
        }
        gen.writeString(result.toPlainString());
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        if (property != null) {
            DecimalFormat ann = property.getAnnotation(DecimalFormat.class);
            if (ann == null) {
                ann = property.getContextAnnotation(DecimalFormat.class);
            }
            if (ann != null) {
                return new FlexibleDecimalSerializer(ann.scale(), ann.stripTrailingZeros(), ann.roundingMode());
            }
        }
        // 返回默认行为（去除末尾零）
        return new FlexibleDecimalSerializer(-1, true, RoundingMode.HALF_UP);
    }






    /**
     * 值
     */
    @DecimalFormat(scale = 2)
    private BigDecimal value;
}