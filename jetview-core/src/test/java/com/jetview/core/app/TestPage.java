package com.jetview.core.app;

import com.jetview.core.annotation.Path;
import com.jetview.core.annotation.RequestParam;
import com.jetview.core.annotation.View;
import com.jetview.core.component.Page;

/**
 * @author Roman Kochergin
 */
@Path("/")
@View("templates/pages/TestPage.peb")
public class TestPage extends Page {
    public TestPage(@RequestParam("title") String title,
                    @RequestParam(name = "empty", defaultValue = "is") String empty,
                    @RequestParam(name = "counter") int counter) {
        addValue("title", () -> "This %s %s".formatted(empty, title));
        addComponent("content", new TestComponent(counter));
    }
}
