package com.jetview.examples.elements;

import com.jetview.core.annotation.Path;
import com.jetview.core.annotation.View;
import com.jetview.core.component.Page;

/**
 * @author Roman Kochergin
 */
@Path("/")
@View("templates/elements/ElementsPage.peb")
public class ElementsPage extends Page {
    public ElementsPage() {
        setComponent("CustomComponent", new CustomComponent());
    }
}
