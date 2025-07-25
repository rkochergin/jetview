package com.jetview.core.converter;

/**
 * @author Roman Kochergin
 */
public final class StringToIntegerConverter implements IConverter<String, Integer> {
    @Override
    public Integer convert(String source) {
        return Integer.valueOf(source);
    }
}
