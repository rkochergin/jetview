package com.jetview.util;

import java.util.function.Supplier;

/**
 * @author Roman Kochergin
 */
public final class LazyValue<T> implements Supplier<T> {

    private volatile T value;

    private final Supplier<T> initializer;

    private LazyValue(Supplier<T> initializer) {
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

    public static <T> LazyValue<T> of(Supplier<T> initializer) {
        return new LazyValue<>(initializer);
    }
}
