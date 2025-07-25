package com.jetview.examples.counter;

import com.jetview.core.annotation.View;
import com.jetview.core.component.Component;

/**
 * @author Roman Kochergin
 */
@View("templates/counter/Counter.peb")
public class Counter extends Component {

    private int value;

    public Counter(int value) {
        this.value = value;
        addValue("value", () -> this.value);
        addValue("className", this::getClassName);
    }

    public void inc() {
        value++;
        notifyStateChange();
    }

    public void dec() {
        value--;
        notifyStateChange();
    }

    private String getClassName() {
        if (value > 0) {
            return "bg-success";
        } else if (value < 0) {
            return "bg-danger";
        } else {
            return "bg-secondary";
        }
    }
}
