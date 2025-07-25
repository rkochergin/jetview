package com.jetview.core.component;

import java.util.Optional;

/**
 * @author Roman Kochergin
 */
public interface Child<T> {
    Optional<T> getParent();
}
