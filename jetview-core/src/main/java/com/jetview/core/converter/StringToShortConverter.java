package com.jetview.core.converter;

/**
 * @author Roman Kochergin
 */
public final class StringToShortConverter implements IConverter<String, Short> {
    @Override
    public Short convert(String source) {
        return Short.valueOf(source);
    }
}
