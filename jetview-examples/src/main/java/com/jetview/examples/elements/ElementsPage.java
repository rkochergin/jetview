package com.jetview.examples.elements;

import com.jetview.core.annotation.Path;
import com.jetview.core.annotation.View;
import com.jetview.core.component.Page;
import com.jetview.core.component.event.Event;
import com.jetview.core.component.event.IEventHandler;

import java.util.Set;

/**
 * @author Roman Kochergin
 */
@Path("/")
@View("templates/elements/ElementsPage.peb")
public class ElementsPage extends Page {
    public ElementsPage() {
        HtmlElement div = new HtmlElement("div");

        HtmlElement h1 = new HtmlElement("h1");
        h1.setTextContent("Header");

        HtmlElement button = new HtmlElement("button");
        button.setTextContent("Button");
        button.addEventListener("click", (IEventHandler) event -> {
                    HtmlElement span = new HtmlElement("span");
                    span.setTextContent("clicked with params: %s".formatted(event.params()));
                    span.addEventListener("click", _ -> div.removeChild(span));
                    div.appendChild(span).appendChild(new HtmlElement("br"));
                },
                Set.of("altKey", "ctrlKey", "shiftKey"));

        div.appendChild(h1).appendChild(button);

        setComponent("HtmlElement", div);
    }
}
