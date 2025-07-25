package com.jetview.examples.counter;

import com.jetview.core.annotation.View;
import com.jetview.core.component.Component;
import com.jetview.core.component.event.AjaxClickBehavior;
import com.jetview.core.component.event.IEventHandler;

/**
 * @author Roman Kochergin
 */
@View("templates/counter/Button.peb")
public class Button extends Component {
    public Button(String name, IEventHandler eventHandler) {
        addValue("name", () -> name);
        addBehavior(new AjaxClickBehavior(eventHandler));
    }
}