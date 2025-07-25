package com.jetview.core.factory;

import com.jetview.core.converter.IConverter;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Roman Kochergin
 */
public class ConverterFactory<S, R> implements IConverterFactory<S, R> {

    private final ConcurrentHashMap<Class<?>, Object> container = new ConcurrentHashMap<>();

    @Override
    public <T extends R> void registerConverter(Class<T> targetType, IConverter<S, T> converter) {
        container.put(targetType, converter);
    }

    @Override
    public <T extends R> void registerConverter(Set<Class<T>> targetTypes, IConverter<S, T> converter) {
        for (Class<T> type : targetTypes) {
            registerConverter(type, converter);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends R> IConverter<S, T> getConverter(Class<T> targetType) {
        return (IConverter<S, T>) container.get(targetType);
    }
}
