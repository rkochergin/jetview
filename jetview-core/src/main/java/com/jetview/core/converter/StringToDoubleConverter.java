package com.jetview.core.converter;

/**
 * @author Roman Kochergin
 */
public final class StringToDoubleConverter implements IConverter<String, Double> {
    @Override
    public Double convert(String source) {
        return Double.valueOf(source);
    }
}
