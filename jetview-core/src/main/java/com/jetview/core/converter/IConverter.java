package com.jetview.core.converter;

/**
 * @author Roman Kochergin
 */
public interface IConverter<S, T> {
    T convert(S source);
}
