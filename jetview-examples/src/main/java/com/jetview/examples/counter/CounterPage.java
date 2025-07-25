package com.jetview.examples.counter;

import com.jetview.core.annotation.Path;
import com.jetview.core.annotation.View;
import com.jetview.core.component.Page;

/**
 * @author Roman Kochergin
 */
@Path("/")
@View("templates/counter/CounterPage.peb")
public class CounterPage extends Page {
    public CounterPage() {
        Counter counter = new Counter(0);
        addComponent("counter", counter);
        addComponent("minusButton", new Button("-", event -> counter.dec()));
        addComponent("plusButton", new Button("+", event -> counter.inc()));
    }
}
