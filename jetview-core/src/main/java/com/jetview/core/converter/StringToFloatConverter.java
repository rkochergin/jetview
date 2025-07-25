package com.jetview.core.converter;

/**
 * @author Roman Kochergin
 */
public final class StringToFloatConverter implements IConverter<String, Float> {
    @Override
    public Float convert(String source) {
        return Float.valueOf(source);
    }
}
