package com.jetview.core.factory;

import com.jetview.core.converter.IConverter;

import java.util.Set;

/**
 * @author Roman Kochergin
 */
public interface IConverterFactory<S, R> {
    <T extends R> void registerConverter(Class<T> targetType, IConverter<S, T> converter);

    <T extends R> void registerConverter(Set<Class<T>> targetTypes, IConverter<S, T> converter);

    <T extends R> IConverter<S, T> getConverter(Class<T> targetType);
}
