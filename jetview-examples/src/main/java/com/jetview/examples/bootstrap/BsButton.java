package com.jetview.examples.bootstrap;

import com.jetview.core.annotation.View;
import com.jetview.core.component.Component;
import com.jetview.core.component.event.Event;
import com.jetview.core.component.event.IEventHandler;

import java.util.Objects;

/**
 * @author Roman Kochergin
 */
//@JsNameSpace("Bootstrap.Button")
@View("templates/bootstrap/Button.peb")
public class BsButton extends Component {

    public enum Style {

        PRIMARY("btn-primary"),
        SECONDARY("btn-secondary"),
        SUCCESS("btn-success"),
        DANGER("btn-danger"),
        WARNING("btn-warning"),
        INFO("btn-info"),
        LIGHT("btn-light"),
        DARK("btn-dark"),
        LINK("btn-link"),
        OUTLINE_PRIMARY("btn-outline-primary"),
        OUTLINE_SECONDARY("btn-outline-secondary"),
        OUTLINE_SUCCESS("btn-outline-success"),
        OUTLINE_DANGER("btn-outline-danger"),
        OUTLINE_WARNING("btn-outline-warning"),
        OUTLINE_INFO("btn-outline-info"),
        OUTLINE_LIGHT("btn-outline-light"),
        OUTLINE_DARK("btn-outline-dark"),
        ;

        private final String value;

        Style(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }

    public enum Size {

        LARGE("btn-lg"),
        NORMAL(""),
        SMALL("btn-sm"),
        ;

        private final String value;

        Size(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }

    private String text = "";

    private Style style = Style.PRIMARY;

    private Size size = Size.NORMAL;

    private boolean enabled = true;

    public BsButton(String text) {
        setText(text);
        setProperty("text", this::getText);
        setProperty("style", this::getStyle);
        setProperty("size", this::getSize);
        setProperty("disabled", () -> enabled ? "" : "disabled");
        setProperty("onClick", () -> hasListener(Event.ON_CLICK) ? "data-onclick" : "");
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        Objects.requireNonNull(text, "text must not be null");
        this.text = text;
        notifyStateChange();
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        Objects.requireNonNull(style, "style must not be null");
        this.style = style;
        notifyStateChange();
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        Objects.requireNonNull(style, "size must not be null");
        this.size = size;
        notifyStateChange();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        notifyStateChange();
    }

    public void setClickHandler(IEventHandler clickHandler) {
        setListener(Event.ON_CLICK, clickHandler);
        notifyStateChange();
    }

}