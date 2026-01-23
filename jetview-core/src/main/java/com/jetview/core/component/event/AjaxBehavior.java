package com.jetview.core.component.event;

import com.jetview.core.component.Component;
import com.jetview.core.util.AjaxUtils;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

public class AjaxBehavior implements IAjaxBehavior {

    private final String event;
    private final Map<String, Serializable> parameters;
    private final IEventHandler eventHandler;

    public AjaxBehavior(String event) {
        this(event, null, null);
    }

    public AjaxBehavior(String event, IEventHandler eventHandler) {
        this(event, null, eventHandler);
    }

    public AjaxBehavior(String event, Map<String, Serializable> parameters) {
        this(event, parameters, null);
    }

    public AjaxBehavior(String event, Map<String, Serializable> parameters, IEventHandler eventHandler) {
        this.event = event;
        this.parameters = parameters;
        this.eventHandler = eventHandler;
    }

    @Override
    public String getEvent() {
        return event;
    }

    @Override
    public String getCallback(Component component) {
        return AjaxUtils.renderAjaxCallback(component, event, parameters);
    }

    @Override
    public IEventHandler getEventHandler() {
        return eventHandler;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AjaxBehavior that)) return false;
        return Objects.equals(event, that.event);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(event);
    }
}
