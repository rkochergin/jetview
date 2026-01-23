package com.jetview.examples.bootstrap;

import com.jetview.core.annotation.View;
import com.jetview.core.component.Renderable;

import java.util.Map;

import static com.jetview.core.app.JetViewContext.getRenderer;

/**
 * @author Roman Kochergin
 */
@View("templates/bootstrap/Toast.peb")
public class BsToast implements Renderable {

    private final Renderable header;
    private final Renderable body;

    public BsToast(Renderable header, Renderable body) {
        this.header = header;
        this.body = body;
    }

    @Override
    public String render() {
        return getRenderer().render(this, Map.of("Header", header, "Body", body));
    }
}
