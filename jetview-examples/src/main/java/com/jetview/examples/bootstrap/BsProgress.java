package com.jetview.examples.bootstrap;

import com.jetview.core.annotation.View;
import com.jetview.core.component.Component;
import com.jetview.core.component.event.IEventHandler;

import java.util.Map;

@View("templates/bootstrap/Progress.peb")
public class BsProgress extends Component {

    private double min = 0.0;
    private double max = 100.0;
    private double value = 0.0;

    public BsProgress() {
        setProperty("min", this::getMin);
        setProperty("max", this::getMax);
        setProperty("value", this::getValue);
        setProperty("percent", this::getPercent);
        setListener("state", _ -> pushState());
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
        pushState();
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
        pushState();
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
        pushState();
    }

    private void pushState() {
        pushState(Map.of(
                "property", "state",
                "min", getMin(),
                "max", getMax(),
                "value", getValue(),
                "percent", getPercent()
        ));
    }

    public int getPercent() {
        return (int) ((getValue() / (getMax() - getMin())) * 100);
    }

    public void setCompleteHandler(IEventHandler completeHandler) {
        setListener("complete", completeHandler);
    }
}
