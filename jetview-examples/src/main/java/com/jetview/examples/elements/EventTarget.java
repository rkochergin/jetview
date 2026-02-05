package com.jetview.examples.elements;

import java.io.Serializable;
import java.util.Set;

/**
 * @author Roman Kochergin
 */
public interface EventTarget<T> extends Serializable {
    void addEventListener(String eventType, T handler);
    void addEventListener(String eventType, T handler, Set<String> eventPropertyRequirements);
    void removeEventListener(String eventType, T handler);
}
