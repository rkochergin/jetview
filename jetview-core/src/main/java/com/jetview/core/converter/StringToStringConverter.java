package com.jetview.core.converter;

/**
 * @author Roman Kochergin
 */
public final class StringToStringConverter implements IConverter<String, String> {
    @Override
    public String convert(String source) {
        return source;
    }
}
