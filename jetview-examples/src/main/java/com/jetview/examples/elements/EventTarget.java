package com.jetview.examples.elements;

import java.io.Serializable;
import java.util.Set;

/**
 * @author Roman Kochergin
 */
public interface EventTarget<T> extends Serializable {
    EventTarget<T> addEventListener(String eventType, T handler);
    EventTarget<T> addEventListener(String eventType, T handler, Set<String> eventPropertyRequirements);
    EventTarget<T> removeEventListener(String eventType, T handler);
}
