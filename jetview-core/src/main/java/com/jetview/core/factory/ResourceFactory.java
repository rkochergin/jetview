package com.jetview.core.factory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * @author Roman Kochergin
 */
public class ResourceFactory implements IResourceFactory {

    private final ConcurrentHashMap<Class<?>, Object> container = new ConcurrentHashMap<>();

    @Override
    public <T, S extends T> void registerResource(Class<T> type, S value) {
        container.put(type, value);
    }

    @Override
    public <T, S extends T> void registerResource(Class<T> type, Supplier<S> value) {
        container.put(type, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T, S extends T> S getResource(Class<T> type) {
        var value = container.get(type);
        if (value instanceof Supplier<?> supplier) {
            return (S) supplier.get();
        } else {
            return (S) value;
        }
    }
}
