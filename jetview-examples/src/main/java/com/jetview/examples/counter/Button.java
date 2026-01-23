package com.jetview.examples.counter;

import com.jetview.core.annotation.View;
import com.jetview.core.component.Component;
import com.jetview.core.component.event.Event;
import com.jetview.core.component.event.IEventHandler;
import com.jetview.core.util.AjaxUtils;

/**
 * @author Roman Kochergin
 */
@View("templates/counter/Button.peb")
public class Button extends Component {
    public Button(String name, IEventHandler eventHandler) {
        setProperty("name", () -> name);
        setProperty(Event.ON_CLICK, () -> AjaxUtils.renderAjaxCallback(this, Event.ON_CLICK, null));
        setListener(Event.ON_CLICK, eventHandler);
    }
}