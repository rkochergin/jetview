package com.jetview.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jetview.core.component.Component;
import com.jetview.core.exception.JetViewRuntimeException;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

import static org.unbescape.html.HtmlEscape.escapeHtml5;

/**
 * @author Roman Kochergin
 */
public class AjaxUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String renderAjaxCallback(Component component, String eventName, Map<String, Serializable> model) {
        return "JV.call('%s', '%s', %s)".formatted(component.getId(), eventName,
                Optional.ofNullable(model).map(m -> {
                    try {
                        return escapeHtml5(OBJECT_MAPPER.writeValueAsString(m));
                    } catch (JsonProcessingException e) {
                        throw new JetViewRuntimeException(e);
                    }
                }).orElse(null));
    }

    private AjaxUtils() {
    }
}
