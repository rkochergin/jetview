package com.jetview.util;

import com.jetview.util.function.SerializableSupplier;

import java.io.Serializable;

/**
 * @author Roman Kochergin
 */
public final class SerializableLazyValue<T extends Serializable> implements SerializableSupplier<T> {

    private volatile T value;

    private final SerializableSupplier<T> initializer;

    private SerializableLazyValue(SerializableSupplier<T> initializer) {
        this.initializer = initializer;
    }

    public T get() {
        if (value == null) {
            synchronized (this) {
                if (value == null) {
                    value = initializer.get();
                }
            }
        }
        return value;
    }

    public static <T extends Serializable> SerializableLazyValue<T> of(SerializableSupplier<T> initializer) {
        return new SerializableLazyValue<>(initializer);
    }
}
