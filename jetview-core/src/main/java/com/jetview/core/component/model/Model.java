package com.jetview.core.component.model;

import com.jetview.util.function.SerializableSupplier;

import java.io.Serializable;
import java.util.Set;

public interface Model extends Serializable {
    void setProperty(String name, SerializableSupplier<Object> value);
    SerializableSupplier<Object> getProperty(String name);
    <T> T getPropertyValue(String name, Class<T> type);
    Set<String> getPropertyNames();
    boolean hasProperty(String name);
    boolean hasProperties();
    void removeProperty(String name);
}
