package com.jetview.core.factory;

import java.util.function.Supplier;

/**
 * @author Roman Kochergin
 */
public interface IResourceFactory {
    <T, S extends T> void registerResource(Class<T> type, S value);

    <T, S extends T> void registerResource(Class<T> type, Supplier<S> value);

    <T, S extends T> S getResource(Class<T> type);
}
