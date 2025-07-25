package com.jetview.core.component.event;

import com.jetview.core.component.Component;

import java.io.Serializable;

public interface IAjaxBehavior extends Serializable {
    String getEvent();

    IEventHandler getEventHandler();

    String getCallback(Component component);
}
