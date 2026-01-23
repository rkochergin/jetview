package com.jetview.core.component;

import java.util.stream.Stream;

/**
 * @author Roman Kochergin
 */
public interface Traversable<T> {
    Stream<T> traverse();
}
