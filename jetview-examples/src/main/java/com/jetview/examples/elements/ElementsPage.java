package com.jetview.examples.elements;

import com.jetview.core.annotation.Path;
import com.jetview.core.annotation.View;
import com.jetview.core.component.Page;

import java.util.Set;

/**
 * @author Roman Kochergin
 */
@Path("/")
@View("templates/elements/ElementsPage.peb")
public class ElementsPage extends Page {
    public ElementsPage() {
        new HtmlElement("div")
                .appendChild(new HtmlElement("h1").setTextContent("Header"))
                .appendChild(
                        new HtmlElement("button")
                                .setTextContent("Button")
                                .addEventListener("click", event -> event.component().getParent()
                                                .filter(HtmlElement.class::isInstance)
                                                .map(HtmlElement.class::cast)
                                                .ifPresent(div -> div.appendChild(new HtmlElement("br"))
                                                        .appendChild(new HtmlElement("span")
                                                                .setTextContent("clicked with params: %s".formatted(event.params()))
                                                                .addEventListener("click", e -> div.removeChild((HtmlElement) e.component())))),
                                        Set.of("altKey", "ctrlKey", "shiftKey"))
                )
                .consume(e -> setComponent("HtmlElement", e));
    }
}
