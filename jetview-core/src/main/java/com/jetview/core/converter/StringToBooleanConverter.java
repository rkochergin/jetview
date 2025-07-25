package com.jetview.core.converter;

/**
 * @author Roman Kochergin
 */
public final class StringToBooleanConverter implements IConverter<String, Boolean> {
    @Override
    public Boolean convert(String source) {
        return Boolean.valueOf(source);
    }
}
