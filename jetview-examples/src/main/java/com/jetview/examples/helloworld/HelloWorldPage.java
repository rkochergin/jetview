package com.jetview.examples.helloworld;

import com.jetview.core.annotation.Path;
import com.jetview.core.annotation.RequestParam;
import com.jetview.core.annotation.View;
import com.jetview.core.component.Page;

/**
 * @author Roman Kochergin
 */
@Path("/")
@View("templates/helloworld/HelloWorldPage.peb")
public class HelloWorldPage extends Page {
    public HelloWorldPage(@RequestParam(name = "name", defaultValue = "World") String name) {
        setProperty("message", () -> "Hello %s!".formatted(name));
    }
}
