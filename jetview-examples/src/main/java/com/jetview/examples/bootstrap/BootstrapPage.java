package com.jetview.examples.bootstrap;

import com.jetview.core.annotation.Path;
import com.jetview.core.annotation.View;
import com.jetview.core.component.Composite;
import com.jetview.core.component.Page;
import com.jetview.core.component.event.IEventHandler;
import com.jetview.util.StringUtils;

import java.util.List;

/**
 * @author Roman Kochergin
 */
@Path("/")
@View("templates/bootstrap/BootstrapPage.peb")
public class BootstrapPage extends Page {
    public BootstrapPage() {

        var toasts = new BsToastContainer();
        toasts.setPosition(BsToastContainer.Position.BOTTOM_RIGHT);
        toasts.setCloseHandler(toast -> {
            toasts.setPosition(BsToastContainer.Position.values()[(toasts.getPosition().ordinal() + 1) % BsToastContainer.Position.values().length]);
            toasts.showToast(toast);
        });
        setComponent("Toasts", toasts);

        setComponent("CustomComponent", new CustomComponent());

        setComponent("RegularButtonStyles", new Composite<>(List.of(
                createButton(getButtonText(BsButton.Style.PRIMARY), BsButton.Style.PRIMARY),
                createButton(getButtonText(BsButton.Style.SECONDARY), BsButton.Style.SECONDARY),
                createButton(getButtonText(BsButton.Style.SUCCESS), BsButton.Style.SUCCESS),
                createButton(getButtonText(BsButton.Style.DANGER), BsButton.Style.DANGER),
                createButton(getButtonText(BsButton.Style.WARNING), BsButton.Style.WARNING),
                createButton(getButtonText(BsButton.Style.INFO), BsButton.Style.INFO),
                createButton(getButtonText(BsButton.Style.LIGHT), BsButton.Style.LIGHT),
                createButton(getButtonText(BsButton.Style.DARK), BsButton.Style.DARK),
                createButton(getButtonText(BsButton.Style.LINK), BsButton.Style.LINK)
        )));
        setComponent("OutlineButtonStyles", new Composite<>(List.of(
                createButton(getButtonText(BsButton.Style.OUTLINE_PRIMARY), BsButton.Style.OUTLINE_PRIMARY),
                createButton(getButtonText(BsButton.Style.OUTLINE_SECONDARY), BsButton.Style.OUTLINE_SECONDARY),
                createButton(getButtonText(BsButton.Style.OUTLINE_SUCCESS), BsButton.Style.OUTLINE_SUCCESS),
                createButton(getButtonText(BsButton.Style.OUTLINE_DANGER), BsButton.Style.OUTLINE_DANGER),
                createButton(getButtonText(BsButton.Style.OUTLINE_WARNING), BsButton.Style.OUTLINE_WARNING),
                createButton(getButtonText(BsButton.Style.OUTLINE_INFO), BsButton.Style.OUTLINE_INFO),
                createButton(getButtonText(BsButton.Style.OUTLINE_LIGHT), BsButton.Style.OUTLINE_LIGHT),
                createButton(getButtonText(BsButton.Style.OUTLINE_DARK), BsButton.Style.OUTLINE_DARK)
        )));
        setComponent("LargeButtonSizes", new Composite<>(List.of(
                createButton("Large button", BsButton.Style.PRIMARY, BsButton.Size.LARGE),
                createButton("Large button", BsButton.Style.SECONDARY, BsButton.Size.LARGE)
        )));
        setComponent("SmallButtonSizes", new Composite<>(List.of(
                createButton("Small button", BsButton.Style.PRIMARY, BsButton.Size.SMALL),
                createButton("Small button", BsButton.Style.SECONDARY, BsButton.Size.SMALL)
        )));
        setComponent("DisabledButtonStates", new Composite<>(List.of(
                createButton("Primary button", BsButton.Style.PRIMARY, BsButton.Size.LARGE, false),
                createButton("Button", BsButton.Style.SECONDARY, BsButton.Size.LARGE, false)
        )));
        setComponent("ButtonEvents", new Composite<>(List.of(
                createButton("Click event", BsButton.Style.PRIMARY, event -> {
                    var toast = new BsToast(
                            new Element("strong")
                                    .setAttribute("class", "me-auto")
                                    .setText("Header"),
                            new Element("small")
                                    .setText("Body"));
                    toasts.showToast(toast);
                })
        )));

    }

    private static BsButton createButton(String text, BsButton.Style style, BsButton.Size size, boolean enabled) {
        var button = new BsButton(text);
        button.setStyle(style);
        button.setSize(size);
        button.setEnabled(enabled);
        return button;
    }

    private static BsButton createButton(String text, BsButton.Style style, BsButton.Size size) {
        return createButton(text, style, size, true);
    }

    private static BsButton createButton(String text, BsButton.Style style) {
        return createButton(text, style, BsButton.Size.NORMAL);
    }

    private static BsButton createButton(String text, BsButton.Style style, IEventHandler clickHandler) {
        var button = createButton(text, style, BsButton.Size.NORMAL);
        button.setClickHandler(clickHandler);
        return button;
    }

    private String getButtonText(BsButton.Style style) {
        var styleTokens = style.name().toLowerCase().split("_");
        return StringUtils.capitalize(styleTokens[styleTokens.length - 1]);
    }

}
