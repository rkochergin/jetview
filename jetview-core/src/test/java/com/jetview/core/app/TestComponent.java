package com.jetview.core.app;

import com.jetview.core.annotation.View;
import com.jetview.core.component.Component;
import com.jetview.core.component.event.Event;

/**
 * @author Roman Kochergin
 */
@View("templates/components/TestComponent.peb")
public class TestComponent extends Component {

    private int counter;

    public TestComponent(int counter) {
        this.counter = counter;
        setProperty("counter", () -> this.counter);
        setListener(Event.ON_CLICK, event -> {
            ++this.counter;
            notifyStateChange();
        });
    }
}
