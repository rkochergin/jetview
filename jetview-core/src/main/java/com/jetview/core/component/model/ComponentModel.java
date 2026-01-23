package com.jetview.core.component.model;

import com.jetview.util.function.SerializableSupplier;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ComponentModel implements Model {

    private final Map<String, Object> properties = new ConcurrentHashMap<>();

    @Override
    public void setProperty(String name, SerializableSupplier<Object> value) {
        if (Objects.isNull(value)) {
            removeProperty(name);
        } else {
            properties.put(name, value);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public SerializableSupplier<Object> getProperty(String name) {
        return (SerializableSupplier<Object>) properties.get(name);
    }

    @Override
    public <T> T getPropertyValue(String name, Class<T> type) {
        return type.cast(getProperty(name).get());
    }

    @Override
    public Set<String> getPropertyNames() {
        return properties.keySet();
    }

    @Override
    public boolean hasProperty(String name) {
        return properties.containsKey(name);
    }

    @Override
    public boolean hasProperties() {
        return !properties.isEmpty();
    }

    @Override
    public void removeProperty(String name) {
        properties.remove(name);
    }
}
