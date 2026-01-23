package com.jetview.core.component.event;

import java.io.Serializable;
import java.util.Map;

public class AjaxClickBehavior extends AjaxBehavior {

    public AjaxClickBehavior() {
        this(null, null);
    }

    public AjaxClickBehavior(Map<String, Serializable> parameters) {
        this(parameters, null);
    }

    public AjaxClickBehavior(IEventHandler eventHandler) {
        this(null, eventHandler);
    }

    public AjaxClickBehavior(Map<String, Serializable> parameters, IEventHandler eventHandler) {
        super(Event.ON_CLICK, parameters, eventHandler);
    }
}
