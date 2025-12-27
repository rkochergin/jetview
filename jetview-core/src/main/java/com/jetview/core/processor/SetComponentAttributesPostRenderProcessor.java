package com.jetview.core.processor;

import com.jetview.core.annotation.JsNameSpace;
import com.jetview.core.component.Component;
import com.jetview.core.component.Page;
import com.jetview.util.ClassUtils;

/**
 * @author Roman Kochergin
 */
public class SetComponentAttributesPostRenderProcessor implements IComponentPostRenderProcessor {

    public static final String DATA_VJ_ID_ATTRIBUTE_NAME = "data-jv-id";
    public static final String DATA_VJ_JS_ATTRIBUTE_NAME = "data-jv-js";

    @Override
    public String process(String output, Component component) {
        if (component instanceof Page p) {
            return processBodyId(output, p);
        } else if (component instanceof Component c) {
            return processComponentId(processComponentJs(output, c), c);
        }
        return output;
    }

    private String processBodyId(String output, Page p) {
        return output.replaceFirst("<body", "<body %s=\"%s\"".formatted(DATA_VJ_ID_ATTRIBUTE_NAME, p.getId()));
    }

    private String processComponentId(String output, Component c) {
        return output.replaceFirst(">", " %s=\"%s\">".formatted(DATA_VJ_ID_ATTRIBUTE_NAME, c.getId()));
    }

    private String processComponentJs(String output, Component c) {
        return ClassUtils.getAnnotation(c.getClass(), JsNameSpace.class)
                .map(JsNameSpace::value)
                .map(js -> output.replaceFirst(">", " %s=\"%s\">".formatted(DATA_VJ_JS_ATTRIBUTE_NAME, js)))
                .orElse(output);
    }
}
