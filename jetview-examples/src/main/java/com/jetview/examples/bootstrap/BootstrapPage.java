package com.jetview.examples.bootstrap;

import com.jetview.core.annotation.Path;
import com.jetview.core.annotation.View;
import com.jetview.core.component.Page;

/**
 * @author Roman Kochergin
 */
@Path("/")
@View("templates/bootstrap/BootstrapPage.peb")
public class BootstrapPage extends Page {
    public BootstrapPage() {
        BsHorizontalLayout horizontalLayout = new BsHorizontalLayout();
        horizontalLayout.add(new BsButton("Left button"), new BsProgress(), new BsButton("Right button"));
        setComponent("HorizontalLayout", horizontalLayout);
    }
}
