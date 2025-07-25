package com.jetview.core.converter;

/**
 * @author Roman Kochergin
 */
public final class StringToByteConverter implements IConverter<String, Byte> {
    @Override
    public Byte convert(String source) {
        return Byte.valueOf(source);
    }
}
