package com.jetview.core.component.event;

import com.jetview.core.component.Component;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

public record Event(Component component, String event, Map<String, Object> params) implements Serializable {

    public static final String ON_CLICK = "onclick";
    public static final String ON_DOUBLE_CLICK = "ondblclick";

    public Event(Component component, String event, Map<String, Object> params) {
        this.component = component;
        this.event = event;
        this.params = Objects.nonNull(params) ? Map.copyOf(params) : null;
    }

    public <T> T getParam(String key, Class<T> paramType) {
        return paramType.cast(params.get(key));
    }

    public <T> T getParam(String key, Class<T> paramType, T defaultValue) {
        return paramType.cast(params.getOrDefault(key, defaultValue));
    }

    @Override
    public String toString() {
        return "Event[" +
                "component=" + component + ", " +
                "event=" + event + ", " +
                "params=" + params + ']';
    }

}
