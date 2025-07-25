package com.jetview.core.converter;

/**
 * @author Roman Kochergin
 */
public final class StringToLongConverter implements IConverter<String, Long> {
    @Override
    public Long convert(String source) {
        return Long.valueOf(source);
    }
}
