package com.jetview.core.processor;

import com.jetview.core.component.Component;
import com.jetview.core.component.Page;

/**
 * @author Roman Kochergin
 */
public class SetComponentAttributeIdPostRenderProcessor implements IComponentPostRenderProcessor {

    public static final String ATTRIBUTE_NAME = "data-jv-id";

    @Override
    public String process(String output, Component component) {
        if (component instanceof Page p) {
            return output.replaceFirst("<body", "<body %s=\"%s\"".formatted(ATTRIBUTE_NAME, p.getId()));
        } else if (component instanceof Component c) {
            return output.replaceFirst(">", " %s=\"%s\">".formatted(ATTRIBUTE_NAME, c.getId()));
        }
        return output;
    }
}
