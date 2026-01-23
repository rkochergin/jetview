package com.jetview.core.component;

import java.util.stream.Stream;

/**
 * @author Roman Kochergin
 */
public interface Parent<T> {
    Stream<T> getChildren();
}
