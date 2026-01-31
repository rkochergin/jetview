package com.jetview.examples.bootstrap;

import com.jetview.core.annotation.Path;
import com.jetview.core.annotation.View;
import com.jetview.core.component.Page;

/**
 * @author Roman Kochergin
 */
@Path("/toasts")
@View("templates/bootstrap/BootstrapToastsPage.peb")
public class BootstrapToastsPage extends Page {
    public BootstrapToastsPage() {

        var toasts = new BsToastContainer();
        toasts.setPosition(BsToastContainer.Position.BOTTOM_RIGHT);
        toasts.setCloseHandler(toast -> {
            toasts.setPosition(BsToastContainer.Position.values()[(toasts.getPosition().ordinal() + 1) % BsToastContainer.Position.values().length]);
            toasts.showToast(toast);
        });
        setComponent("Toasts", toasts);

        var button = new BsButton("Show toast");
        button.setClickHandler(_ -> {
            var toast = new BsToast(
                    new Element("strong")
                            .setAttribute("class", "me-auto")
                            .setText("Header"),
                    new Element("small")
                            .setText("Body"));
            toasts.showToast(toast);
        });
        setComponent("Button", button);
    }
}
