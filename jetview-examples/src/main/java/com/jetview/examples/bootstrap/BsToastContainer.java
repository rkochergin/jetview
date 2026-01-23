package com.jetview.examples.bootstrap;

import com.jetview.core.annotation.View;
import com.jetview.core.component.Component;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static com.jetview.examples.bootstrap.BsToastContainer.Position.TOP_LEFT;

@View("templates/bootstrap/ToastContainer.peb")
public class BsToastContainer extends Component {

    private static final AtomicInteger ID_GENERATOR = new AtomicInteger();

    private final Map<Integer, BsToast> toasts = new ConcurrentHashMap<>();

    public enum Position {
        TOP_LEFT,
        TOP_CENTER,
        TOP_RIGHT,
        MIDDLE_LEFT,
        MIDDLE_CENTER,
        MIDDLE_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_CENTER,
        BOTTOM_RIGHT,
    }

    private Position position = TOP_LEFT;

    private ToastCloseHandler closeHandler;

    public BsToastContainer() {
        setProperty("position", this::getPosition);
        setListener("onClosed", event -> {
            var toastId = event.getParam("toastId", Integer.class);
            var toast = toasts.remove(toastId);
            if (closeHandler != null) {
                closeHandler.onClose(toast);
            }
        });
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        Objects.requireNonNull(position, "position must not be null");
        var oldPosition = this.position;
        this.position = position;
        notifyStateChange(Map.of(
                "property", "position",
                "oldValue", oldPosition.name(),
                "newValue", position.name()));
    }

    public void showToast(BsToast toast) {
        Objects.requireNonNull(toast, "toast must not be null");
        var id = ID_GENERATOR.incrementAndGet();
        toasts.put(id, toast);
        notifyStateChange(Map.of(
                "action", "show",
                "toastId", id,
                "markup", toast.render()));
    }

    public void setCloseHandler(ToastCloseHandler closeHandler) {
        this.closeHandler = closeHandler;
    }

    @FunctionalInterface
    public interface ToastCloseHandler extends Serializable {
        void onClose(BsToast toast);
    }
}
