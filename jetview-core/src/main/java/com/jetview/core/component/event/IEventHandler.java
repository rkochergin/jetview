package com.jetview.core.component.event;

import java.io.Serializable;

public interface IEventHandler extends Serializable {
    void onEvent(Event event);
}
