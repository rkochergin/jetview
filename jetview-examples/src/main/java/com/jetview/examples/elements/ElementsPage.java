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
//        setComponent("CustomComponent", new CustomComponent());

        HtmlElement h1 = new HtmlElement("h1");
        h1.setTextContent("Header");

        HtmlElement button = new HtmlElement("button");
        button.setTextContent("Button");
        button.setAttribute("is", "jv-el-button");
        button.addEventListener("click", new IEventHandler() {
            @Override
            public void onEvent(Event event) {
                System.out.println("event = " + event);
                button.removeEventListener(event.type(), this);
                button.addEventListener(event.type(), this, Set.of("shiftKey"));
            }
        }, Set.of("altKey"));

        HtmlElement div = new HtmlElement("div");
        div.appendChild(h1).appendChild(button);

        setComponent("HtmlElement", div);
    }
}
