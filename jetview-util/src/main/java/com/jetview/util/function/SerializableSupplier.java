package com.jetview.util.function;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * @author Roman Kochergin
 */
@FunctionalInterface
public interface SerializableSupplier<T> extends Supplier<T>, Serializable {
    // Only method inherited from Supplier
}
