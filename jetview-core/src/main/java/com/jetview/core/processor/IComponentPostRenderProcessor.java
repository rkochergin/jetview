package com.jetview.core.processor;

import com.jetview.core.component.Component;

/**
 * @author Roman Kochergin
 */
@FunctionalInterface
public interface IComponentPostRenderProcessor {
    String process(String output, Component component);
}
